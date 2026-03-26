package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.CompanionCheckinDTO;
import com.PJDM.dto.CompanionScheduleDTO;
import com.PJDM.pojo.OrderEvaluation;
import com.PJDM.pojo.ServiceAccompanistSchedule;
import com.PJDM.pojo.ServiceCheckin;
import com.PJDM.service.IOrderEvaluationService;
import com.PJDM.service.IServiceAccompanistScheduleService;
import com.PJDM.service.IServiceCheckinService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 陪诊师端 - 日程 / 打卡 / 评价
 */
@Slf4j
@RestController
@RequestMapping("/companion")
@Tag(name = "陪诊师端-日程打卡评价", description = "陪诊师日程管理、打卡签到、评价查询与回复")
public class CompanionScheduleController {

    @Autowired private IServiceAccompanistScheduleService scheduleService;
    @Autowired private IServiceCheckinService checkinService;
    @Autowired private IOrderEvaluationService evaluationService;

    // ==================== 日程管理 ====================

    @GetMapping("/schedule")
    @Operation(summary = "获取日程列表")
    public R<IPage<ServiceAccompanistSchedule>> getSchedule(
            @RequestParam Long accompanistId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Byte status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        LambdaQueryWrapper<ServiceAccompanistSchedule> w =
                new LambdaQueryWrapper<ServiceAccompanistSchedule>()
                        .eq(ServiceAccompanistSchedule::getAccompanistId, accompanistId)
                        .eq(status != null, ServiceAccompanistSchedule::getScheduleStatus, status)
                        .eq(StringUtils.hasText(date), ServiceAccompanistSchedule::getScheduleDate,
                                StringUtils.hasText(date) ? LocalDate.parse(date) : null)
                        .orderByAsc(ServiceAccompanistSchedule::getScheduleDate);
        return R.success(scheduleService.page(new Page<>(page, pageSize), w));
    }

    @PutMapping("/schedule/{id}")
    @Operation(summary = "更新日程状态")
    public R<String> updateSchedule(@PathVariable Long id,
                                    @RequestBody CompanionScheduleDTO dto) {
        ServiceAccompanistSchedule s = scheduleService.getById(id);
        if (s == null) return R.error("日程不存在");
        if (dto.getScheduleStatus() != null) s.setScheduleStatus(dto.getScheduleStatus());
        if (StringUtils.hasText(dto.getRemark())) s.setRemark(dto.getRemark());
        s.setUpdateTime(LocalDateTime.now());
        scheduleService.updateById(s);
        return R.success("更新成功");
    }

    // ==================== 打卡管理 ====================

    @PostMapping("/checkin")
    @Operation(summary = "打卡签到")
    public R<String> checkin(@RequestParam Long accompanistId,
                             @RequestBody CompanionCheckinDTO dto) {
        if (dto.getOrderId() == null) return R.error("orderId 不能为空");
        ServiceCheckin c = new ServiceCheckin();
        c.setOrderId(dto.getOrderId());
        c.setAccompanistId(accompanistId);
        c.setLatitude(dto.getLatitude());
        c.setLongitude(dto.getLongitude());
        c.setCheckinLocation(dto.getAddress());
        c.setCheckinPhotos(dto.getPhoto());
        c.setCheckinType(dto.getCheckinType() != null ? dto.getCheckinType() : (byte) 7);
        c.setCheckinDescription(dto.getDescription());
        c.setCheckinTime(LocalDateTime.now());
        c.setCreateTime(LocalDateTime.now());
        checkinService.save(c);
        log.info("[打卡] accompanistId={} orderId={} type={}", accompanistId, dto.getOrderId(), c.getCheckinType());
        return R.success("打卡成功");
    }

    @GetMapping("/checkin/{orderId}")
    @Operation(summary = "获取打卡记录")
    public R<List<ServiceCheckin>> getCheckinRecords(@PathVariable Long orderId) {
        LambdaQueryWrapper<ServiceCheckin> w = new LambdaQueryWrapper<ServiceCheckin>()
                .eq(ServiceCheckin::getOrderId, orderId)
                .orderByAsc(ServiceCheckin::getCheckinTime);
        return R.success(checkinService.list(w));
    }

    // ==================== 评价管理 ====================

    @GetMapping("/evaluations")
    @Operation(summary = "获取收到的评价列表")
    public R<IPage<OrderEvaluation>> getEvaluations(
            @RequestParam Long accompanistId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<OrderEvaluation> w = new LambdaQueryWrapper<OrderEvaluation>()
                .eq(OrderEvaluation::getAccompanistId, accompanistId)
                .orderByDesc(OrderEvaluation::getCreateTime);
        return R.success(evaluationService.page(new Page<>(page, pageSize), w));
    }

    @PostMapping("/evaluations/{id}/reply")
    @Operation(summary = "回复评价")
    public R<String> replyEvaluation(@PathVariable Long id,
                                     @RequestBody Map<String, String> body) {
        OrderEvaluation eval = evaluationService.getById(id);
        if (eval == null) return R.error("评价不存在");
        eval.setEvaluationReply(body.get("evaluation_reply"));
        eval.setReplyTime(LocalDateTime.now());
        evaluationService.updateById(eval);
        log.info("[评价回复] evaluationId={}", id);
        return R.success("回复成功");
    }
}
