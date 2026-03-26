package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.AppointmentQueryDTO;
import com.PJDM.dto.AppointmentUpdateDTO;
import com.PJDM.pojo.MedicalAppointment;
import com.PJDM.service.IMedicalAppointmentService;
import com.PJDM.vo.AppointmentListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/medical/appointment")
@Tag(name = "预约管理", description = "预约信息增删改查及状态管理")
public class MedicalAppointmentController {

    @Autowired private IMedicalAppointmentService appointmentService;

    @GetMapping("/list")
    @Operation(summary = "分页查询预约列表")
    public R<IPage<AppointmentListVO>> list(AppointmentQueryDTO query) {
        return R.success(appointmentService.getAppointmentListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取预约详情")
    public R<MedicalAppointment> detail(@PathVariable Long id) {
        try { return R.success(appointmentService.getAppointmentDetail(id)); }
        catch (RuntimeException e) { return R.error(e.getMessage()); }
    }

    @PostMapping
    @Operation(summary = "新增预约")
    public R<String> add(@RequestBody AppointmentUpdateDTO dto) {
        try { appointmentService.addAppointment(dto); return R.success("新增成功"); }
        catch (RuntimeException e) { return R.error(e.getMessage()); }
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改预约")
    public R<String> update(@PathVariable Long id, @RequestBody AppointmentUpdateDTO dto) {
        try { appointmentService.updateAppointment(id, dto); return R.success("修改成功"); }
        catch (RuntimeException e) { return R.error(e.getMessage()); }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除预约")
    public R<String> delete(@PathVariable Long id) {
        try { appointmentService.deleteAppointment(id); return R.success("删除成功"); }
        catch (RuntimeException e) { return R.error(e.getMessage()); }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "变更预约状态（2已确认 3已取消 4已完成 5已过期）")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, Byte> body) {
        try {
            Byte status = body.get("status");
            if (status == null) return R.error("status不能为空");
            appointmentService.updateStatus(id, status); return R.success("状态变更成功");
        } catch (RuntimeException e) { return R.error(e.getMessage()); }
    }
}
