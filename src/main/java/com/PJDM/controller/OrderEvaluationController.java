package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.EvaluationQueryDTO;
import com.PJDM.dto.EvaluationSubmitDTO;
import com.PJDM.pojo.OrderEvaluation;
import com.PJDM.service.IOrderEvaluationService;
import com.PJDM.vo.EvaluationListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订单评价 Controller
 */
@Slf4j
@RestController
@RequestMapping("/order/evaluation")
@Tag(name = "订单评价管理", description = "评价查询与删除")
public class OrderEvaluationController {

    @Autowired
    private IOrderEvaluationService evaluationService;

    @PostMapping("/submit")
    @Operation(summary = "患者提交评价")
    public R<String> submit(@RequestBody EvaluationSubmitDTO dto) {
        try {
            evaluationService.submitEvaluation(dto);
            return R.success("评价成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询评价列表（LEFT JOIN user_user、user_accompanist）")
    public R<IPage<EvaluationListVO>> list(EvaluationQueryDTO query) {
        return R.success(evaluationService.getEvaluationListPage(query));
    }

    @GetMapping("/status")
    @Operation(summary = "批量查询订单是否已评价")
    public R<Map<String, Boolean>> status(@RequestParam(required = false) String orderNos) {
        if (orderNos == null || orderNos.isBlank()) {
            return R.success(Collections.emptyMap());
        }
        List<String> orderNoList = Arrays.stream(orderNos.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        if (orderNoList.isEmpty()) {
            return R.success(Collections.emptyMap());
        }
        Set<String> evaluatedOrderNoSet = evaluationService.getEvaluatedOrderNos(orderNoList)
                .stream()
                .collect(Collectors.toSet());
        Map<String, Boolean> statusMap = new LinkedHashMap<>();
        for (String orderNo : orderNoList) {
            statusMap.put(orderNo, evaluatedOrderNoSet.contains(orderNo));
        }
        return R.success(statusMap);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取评价详情")
    public R<OrderEvaluation> detail(@PathVariable Long id) {
        try {
            return R.success(evaluationService.getEvaluationDetail(id));
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评价")
    public R<String> delete(@PathVariable Long id) {
        try {
            evaluationService.deleteEvaluation(id);
            return R.success("删除成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
