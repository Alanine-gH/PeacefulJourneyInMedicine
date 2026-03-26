package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.ExpertQueryDTO;
import com.PJDM.dto.ExpertUpdateDTO;
import com.PJDM.exception.ValidationException;
import com.PJDM.pojo.MedicalExpert;
import com.PJDM.service.IMedicalExpertService;
import com.PJDM.vo.ExpertListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/medical/expert")
@Tag(name = "专家管理", description = "专家信息增删改查")
public class MedicalExpertController {

    @Autowired private IMedicalExpertService expertService;

    @GetMapping("/list")
    @Operation(summary = "分页查询专家列表")
    public R<IPage<ExpertListVO>> list(ExpertQueryDTO query) {
        log.info("[专家] 列表查询 query={}", query);
        return R.success(expertService.getExpertListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取专家详情")
    public R<MedicalExpert> detail(@PathVariable Long id) {
        log.info("[专家] 详情 id={}", id);
        return R.success(expertService.getExpertDetail(id));
    }

    @PostMapping
    @Operation(summary = "新增专家")
    public R<String> add(@RequestBody ExpertUpdateDTO dto) {
        log.info("[专家] 新增 name={}", dto.getExpertName());
        expertService.addExpert(dto);
        return R.success("新增成功");
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改专家")
    public R<String> update(@PathVariable Long id, @RequestBody ExpertUpdateDTO dto) {
        log.info("[专家] 修改 id={}", id);
        expertService.updateExpert(id, dto);
        return R.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除专家")
    public R<String> delete(@PathVariable Long id) {
        log.info("[专家] 删除 id={}", id);
        expertService.deleteExpert(id);
        return R.success("删除成功");
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "修改专家状态（1正常 2停用）")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, Byte> body) {
        Byte status = body.get("status");
        if (status == null) throw new ValidationException("status", "不能为空");
        log.info("[专家] 状态变更 id={} status={}", id, status);
        expertService.updateStatus(id, status);
        return R.success("修改成功");
    }
}
