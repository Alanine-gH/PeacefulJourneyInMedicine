package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.pojo.MedicalRecordUpload;
import com.PJDM.service.IMedicalRecordUploadService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

/**
 * 患者端 - 病历 / 检查报告接口
 */
@Slf4j
@RestController
@Tag(name = "患者端-病历报告", description = "电子病历与检查报告上传、查询")
public class RecordController {

    @Autowired private IMedicalRecordUploadService recordUploadService;

    @PostMapping("/api/medical-record/save")
    @Operation(summary = "保存病历（小程序上传病历）")
    public R<String> saveMedicalRecord(@RequestBody Map<String, Object> body) {
        System.err.println("body = " + body);
        log.info("[病历] 保存病历 userId={}", body.get("userId"));
        MedicalRecordUpload record = new MedicalRecordUpload();
        if (body.get("userId") != null) {
            try { record.setUserId(Long.parseLong(body.get("userId").toString())); }
            catch (NumberFormatException ignored) {}
        }
        record.setFileUrls(body.get("imageUrl") != null ? body.get("imageUrl").toString() : null);
        record.setRecordTitle(body.get("diagnosis") != null ? body.get("diagnosis").toString() : null);
        record.setHospitalName(body.get("hospital") != null ? body.get("hospital").toString() : null);
        record.setDepartmentName(body.get("department") != null ? body.get("department").toString() : null);
//        record.setVisitDate(body.get("visitDate") != null ? body.get("visitDate").toString() : null);
        record.setRemark(body.get("remark") != null ? body.get("remark").toString() : null);
        record.setCreateTime(LocalDateTime.now());
        // 生成唯一病历编号：REC + yyyyMMddHHmmss + 6位随机串
        String recordNo = "REC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        record.setRecordNo(recordNo);
        recordUploadService.save(record);
        return R.success("保存成功");
    }

    @GetMapping("/medical/records")
    @Operation(summary = "获取病历列表")
    public R<IPage<MedicalRecordUpload>> getMedicalRecords(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalRecordUpload> w = new LambdaQueryWrapper<MedicalRecordUpload>()
                .eq(userId != null, MedicalRecordUpload::getUserId, userId)
                .orderByDesc(MedicalRecordUpload::getCreateTime);
        return R.success(recordUploadService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/medical/records/{recordId}")
    @Operation(summary = "获取病历详情")
    public R<MedicalRecordUpload> getMedicalRecordDetail(@PathVariable Long recordId) {
        return R.success(recordUploadService.getById(recordId));
    }

    @PostMapping("/medical/report/upload")
    @Operation(summary = "上传检查报告")
    public R<String> uploadMedicalReport(
            @RequestParam Long userId,
            @RequestBody Map<String, Object> body) {
        log.info("[病历] 上传报告 userId={}", userId);
        MedicalRecordUpload record = new MedicalRecordUpload();
        record.setUserId(userId);
        record.setFileUrls(body.get("report_file") != null ? body.get("report_file").toString() : null);
        record.setRecordTitle(body.get("description") != null ? body.get("description").toString() : null);
        if (body.get("report_type") != null) {
            try { record.setRecordType(Byte.parseByte(body.get("report_type").toString())); }
            catch (NumberFormatException ignored) {}
        }
        // hospital_id can be used for lookup if needed in future extension
        record.setCreateTime(LocalDateTime.now());
        // 生成唯一病历编号
        String recordNo = "REC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        record.setRecordNo(recordNo);
        recordUploadService.save(record);
        return R.success("上传成功");
    }

    @GetMapping("/medical/reports")
    @Operation(summary = "获取检查报告列表")
    public R<IPage<MedicalRecordUpload>> getMedicalReports(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Byte reportType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalRecordUpload> w = new LambdaQueryWrapper<MedicalRecordUpload>()
                .eq(userId != null, MedicalRecordUpload::getUserId, userId)
                .eq(reportType != null, MedicalRecordUpload::getRecordType, reportType)
                .orderByDesc(MedicalRecordUpload::getCreateTime);
        return R.success(recordUploadService.page(new Page<>(page, pageSize), w));
    }
}
