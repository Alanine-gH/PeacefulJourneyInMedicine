package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.HospitalQueryDTO;
import com.PJDM.dto.HospitalUpdateDTO;
import com.PJDM.exception.ValidationException;
import com.PJDM.pojo.MedicalHospital;
import com.PJDM.service.IMedicalHospitalService;
import com.PJDM.vo.HospitalListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/medical/hospital")
@Tag(name = "医院管理", description = "医院信息增删改查")
public class MedicalHospitalController {

    @Autowired private IMedicalHospitalService hospitalService;

    @GetMapping("/stat/count")
    @Operation(summary = "查询医院总数")
    public R<Long> count() {
        return R.success(hospitalService.count());
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询医院列表")
    public R<IPage<HospitalListVO>> list(HospitalQueryDTO query) {
        log.info("[医院] 列表查询 query={}", query);
        return R.success(hospitalService.getHospitalListPage(query));
    }

    @GetMapping("/search-by-region")
    @Operation(summary = "按地区搜索医院列表")
    public R<IPage<HospitalListVO>> searchByRegion(HospitalQueryDTO query) {
        log.info("[医院] 按地区搜索 query={}", query);
        return R.success(hospitalService.searchByRegion(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取医院详情")
    public R<MedicalHospital> detail(@PathVariable Long id) {
        log.info("[医院] 详情 id={}", id);
        return R.success(hospitalService.getHospitalDetail(id));
    }

    @PostMapping
    @Operation(summary = "新增医院")
    public R<String> add(@RequestBody HospitalUpdateDTO dto) {
        log.info("[医院] 新增 name={}", dto.getHospitalName());
        hospitalService.addHospital(dto);
        return R.success("新增成功");
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改医院")
    public R<String> update(@PathVariable Long id, @RequestBody HospitalUpdateDTO dto) {
        log.info("[医院] 修改 id={}", id);
        hospitalService.updateHospital(id, dto);
        return R.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除医院")
    public R<String> delete(@PathVariable Long id) {
        log.info("[医院] 删除 id={}", id);
        hospitalService.deleteHospital(id);
        return R.success("删除成功");
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "修改医院状态（1正常 2停用）")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, Byte> body) {
        Byte status = body.get("status");
        if (status == null) throw new ValidationException("status", "不能为空");
        log.info("[医院] 状态变更 id={} status={}", id, status);
        hospitalService.updateStatus(id, status);
        return R.success("修改成功");
    }
}
