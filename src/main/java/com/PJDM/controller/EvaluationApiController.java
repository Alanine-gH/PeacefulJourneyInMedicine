package com.PJDM.controller;


import com.PJDM.common.R;
import com.PJDM.dto.EvaluationCreateDTO;
import com.PJDM.pojo.OrderEvaluation;
import com.PJDM.service.IOrderEvaluationService;
import com.PJDM.vo.EvaluationStatVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 患者端 - 评价接口（补充路径，核心列表/详情由 OrderEvaluationController 提供）
 */
@Slf4j
@RestController
@Tag(name = "患者端-评价", description = "患者端评价查询与管理")
public class EvaluationApiController {

    @Autowired private IOrderEvaluationService evaluationService;

    @GetMapping("/evaluation/my")
    @Operation(summary = "获取我的评价列表")
    public R<IPage<OrderEvaluation>> getMyEvaluations(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<OrderEvaluation> w = new LambdaQueryWrapper<OrderEvaluation>()
                .eq(OrderEvaluation::getUserId, userId)
                .orderByDesc(OrderEvaluation::getCreateTime);
        return R.success(evaluationService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/evaluation/pending")
    @Operation(summary = "获取待评价订单（已完成未评价）")
    public R<String> getPendingEvaluations(@RequestParam Long userId) {
        return R.success("请通过订单列表查询状态=5（已完成）且无评价记录的订单");
    }

    @PostMapping("/patient/orders/evaluate")
    @Operation(summary = "患者评价订单")
    public R<String> evaluateOrder(@RequestBody EvaluationCreateDTO dto) {
        log.info("[评价] orderNo={} score={}", dto.getOrderNo(), dto.getRatingScore());
        OrderEvaluation eval = new OrderEvaluation();
        eval.setOrderNo(dto.getOrderNo());
        eval.setRatingScore(dto.getRatingScore());
        eval.setEvaluationContent(dto.getEvaluationContent());
        eval.setEvaluationImages(dto.getEvaluationImages());
        if (dto.getRatingLabels() != null) {
            eval.setRatingLabels(String.join(",", dto.getRatingLabels()));
        }
        eval.setCreateTime(LocalDateTime.now());
        evaluationService.save(eval);
        return R.success("评价成功");
    }

    @DeleteMapping("/evaluation/delete/{evaluationId}")
    @Operation(summary = "删除评价（患者端）")
    public R<String> deleteEvaluation(@PathVariable Long evaluationId) {
        evaluationService.removeById(evaluationId);
        return R.success("删除成功");
    }

    @PostMapping("/evaluation/like/{evaluationId}")
    @Operation(summary = "点赞评价")
    public R<String> likeEvaluation(@PathVariable Long evaluationId) {
        return R.success("点赞成功");
    }

    @PostMapping("/evaluation/unlike/{evaluationId}")
    @Operation(summary = "取消点赞")
    public R<String> unlikeEvaluation(@PathVariable Long evaluationId) {
        return R.success("取消点赞成功");
    }

    @GetMapping("/evaluation/statistics")
    @Operation(summary = "获取评价统计")
    public R<EvaluationStatVO> getEvaluationStatistics(
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) Long targetId) {
        LambdaQueryWrapper<OrderEvaluation> w = new LambdaQueryWrapper<OrderEvaluation>()
                .eq(targetId != null, OrderEvaluation::getAccompanistId, targetId);
        List<OrderEvaluation> list = evaluationService.list(w);
        EvaluationStatVO stat = new EvaluationStatVO();
        stat.setTargetType(targetType);
        stat.setTargetId(targetId);
        stat.setTotalCount((long) list.size());
        BigDecimal avg = list.stream()
                .filter(e -> e.getRatingScore() != null)
                .map(OrderEvaluation::getRatingScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stat.setAvgScore(list.isEmpty() ? BigDecimal.ZERO
                : avg.divide(BigDecimal.valueOf(list.size()), 2, RoundingMode.HALF_UP));
        Map<Integer, Long> dist = list.stream()
                .filter(e -> e.getRatingScore() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getRatingScore().intValue(), Collectors.counting()));
        stat.setScoreDistribution(dist);
        return R.success(stat);
    }

    @PostMapping("/evaluation/reply/{evaluationId}")
    @Operation(summary = "回复评价")
    public R<String> replyEvaluation(@PathVariable Long evaluationId,
                                      @RequestBody Map<String, String> body) {
        OrderEvaluation eval = evaluationService.getById(evaluationId);
        if (eval == null) return R.error("评价不存在");
        eval.setEvaluationReply(body.get("content"));
        eval.setReplyTime(LocalDateTime.now());
        evaluationService.updateById(eval);
        return R.success("回复成功");
    }

    @GetMapping("/evaluation/replies/{evaluationId}")
    @Operation(summary = "获取评价回复列表")
    public R<Object> getEvaluationReplies(
            @PathVariable Long evaluationId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        OrderEvaluation eval = evaluationService.getById(evaluationId);
        if (eval == null) return R.error("评价不存在");
        List<Map<String, Object>> replies = new ArrayList<>();
        if (StringUtils.hasText(eval.getEvaluationReply())) {
            replies.add(Map.of(
                    "id", eval.getId(),
                    "content", eval.getEvaluationReply(),
                    "replyTime", eval.getReplyTime() != null ? eval.getReplyTime().toString() : ""
            ));
        }
        return R.success(replies);
    }

    @DeleteMapping("/evaluation/reply/delete/{replyId}")
    @Operation(summary = "删除评价回复")
    public R<String> deleteEvaluationReply(@PathVariable Long replyId) {
        OrderEvaluation eval = evaluationService.getById(replyId);
        if (eval == null) return R.error("评价不存在");
        eval.setEvaluationReply(null);
        eval.setReplyTime(null);
        evaluationService.updateById(eval);
        return R.success("删除成功");
    }
}
