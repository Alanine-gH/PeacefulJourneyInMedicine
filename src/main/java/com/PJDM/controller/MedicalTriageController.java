package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.TriageQueryDTO;
import com.PJDM.pojo.MedicalTriageRecord;
import com.PJDM.service.IMedicalTriageService;
import com.PJDM.vo.TriageListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/medical/triage")
@Tag(name = "智能分诊管理", description = "分诊记录查询、状态变更、删除")
public class MedicalTriageController {

    @Autowired private IMedicalTriageService triageService;

    @GetMapping("/list")
    @Operation(summary = "分页查询分诊记录")
    public R<IPage<TriageListVO>> list(TriageQueryDTO query) {
        return R.success(triageService.getTriageListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分诊详情")
    public R<MedicalTriageRecord> detail(@PathVariable Long id) {
        try { return R.success(triageService.getTriageDetail(id)); }
        catch (RuntimeException e) { return R.error(e.getMessage()); }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "变更分诊状态（5人工复审完成 6已生成方案等）")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, Byte> body) {
        try {
            Byte status = body.get("status");
            if (status == null) return R.error("status不能为空");
            triageService.updateTriageStatus(id, status); return R.success("状态变更成功");
        } catch (RuntimeException e) { return R.error(e.getMessage()); }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分诊记录")
    public R<String> delete(@PathVariable Long id) {
        try { triageService.deleteTriage(id); return R.success("删除成功"); }
        catch (RuntimeException e) { return R.error(e.getMessage()); }
    }
}
