package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.pojo.MedicalAdviceBook;
import com.PJDM.pojo.MedicalTriageRecord;
import com.PJDM.service.IMedicalAdviceBookService;
import com.PJDM.service.IMedicalTriageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 患者端 - 分诊 / 就医建议书接口
 */
@Slf4j
@RestController
@Tag(name = "患者端-分诊建议", description = "患者端分诊记录提交、查询、就医建议书获取")
public class TriageApiController {

    @Autowired
    private IMedicalTriageService triageService;
    @Autowired
    private IMedicalAdviceBookService adviceBookService;

    // ==================== 分诊管理 ====================

    @PostMapping("/triage/records")
    @Operation(summary = "创建分诊记录")
    public R<String> createTriageRecord(@RequestBody MedicalTriageRecord record) {
        log.info("[分诊] 创建 userId={}", record.getUserId());
        record.setCreateTime(LocalDateTime.now());
        triageService.save(record);
        return R.success("分诊记录已创建");
    }

    @GetMapping("/triage/records")
    @Operation(summary = "获取分诊记录列表")
    public R<IPage<MedicalTriageRecord>> getTriageRecords(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalTriageRecord> w = new LambdaQueryWrapper<MedicalTriageRecord>()
                .eq(userId != null, MedicalTriageRecord::getUserId, userId)
                .orderByDesc(MedicalTriageRecord::getCreateTime);
        return R.success(triageService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/triage/records/{triageId}")
    @Operation(summary = "获取分诊记录详情")
    public R<MedicalTriageRecord> getTriageRecordDetail(@PathVariable Long triageId) {
        return R.success(triageService.getTriageDetail(triageId));
    }

    // ==================== 就医建议书 ====================

    @GetMapping("/triage/advice/{triageId}")
    @Operation(summary = "获取就医建议书")
    public R<MedicalAdviceBook> getMedicalAdvice(@PathVariable Long triageId) {
        MedicalAdviceBook advice = adviceBookService.getOne(
                new LambdaQueryWrapper<MedicalAdviceBook>()
                        .eq(MedicalAdviceBook::getTriageId, triageId)
                        .eq(MedicalAdviceBook::getAdviceStatus, (byte) 3)  // 已发布
                        .orderByDesc(MedicalAdviceBook::getCreateTime)
                        .last("LIMIT 1"));
        if (advice == null) return R.error("就医建议书暂未生成，请等待");
        return R.success(advice);
    }

    @GetMapping("/triage/advice/list")
    @Operation(summary = "获取就医建议书列表")
    public R<IPage<MedicalAdviceBook>> getAdviceList(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalAdviceBook> w = new LambdaQueryWrapper<MedicalAdviceBook>()
                .eq(userId != null, MedicalAdviceBook::getUserId, userId)
                .orderByDesc(MedicalAdviceBook::getCreateTime);
        return R.success(adviceBookService.page(new Page<>(page, pageSize), w));
    }
}
