package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.AppointmentUpdateDTO;
import com.PJDM.pojo.MedicalAppointment;
import com.PJDM.service.IMedicalAppointmentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 患者端 - 预约管理接口
 */
@Slf4j
@RestController
@Tag(name = "患者端-预约管理", description = "患者端预约创建、查询、取消")
public class AppointmentApiController {

    @Autowired private IMedicalAppointmentService appointmentService;

    @PostMapping("/appointment/create")
    @Operation(summary = "创建预约")
    public R<String> createAppointment(@RequestBody AppointmentUpdateDTO dto) {
        log.info("[预约] 创建 hospitalId={} departmentId={}", dto.getHospitalId(), dto.getDepartmentId());
        appointmentService.addAppointment(dto);
        return R.success("预约成功");
    }

    @GetMapping("/appointment/list")
    @Operation(summary = "获取预约列表")
    public R<IPage<MedicalAppointment>> getAppointments(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Byte status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalAppointment> w = new LambdaQueryWrapper<MedicalAppointment>()
                .eq(userId != null, MedicalAppointment::getUserId, userId)
                .eq(status != null, MedicalAppointment::getAppointmentStatus, status)
                .orderByDesc(MedicalAppointment::getCreateTime);
        return R.success(appointmentService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/appointment/detail/{appointmentId}")
    @Operation(summary = "获取预约详情")
    public R<MedicalAppointment> getAppointmentDetail(@PathVariable Long appointmentId) {
        return R.success(appointmentService.getAppointmentDetail(appointmentId));
    }

    @PostMapping("/appointment/cancel/{appointmentId}")
    @Operation(summary = "取消预约")
    public R<String> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestBody(required = false) Map<String, String> body) {
        log.info("[预约] 取消 id={}", appointmentId);
        // 状态3=已取消
        appointmentService.updateStatus(appointmentId, (byte) 3);
        return R.success("取消成功");
    }
}
