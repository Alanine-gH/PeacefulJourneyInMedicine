package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.pojo.MedicalDepartment;
import com.PJDM.pojo.MedicalExpert;
import com.PJDM.pojo.MedicalHospital;
import com.PJDM.service.IMedicalDepartmentService;
import com.PJDM.service.IMedicalExpertService;
import com.PJDM.service.IMedicalHospitalService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者端 - 医院 / 科室 / 专家 公开查询接口
 */
@Slf4j
@RestController
@Tag(name = "患者端-医院科室专家", description = "患者端医院、科室、专家查询")
public class HospitalApiController {

    @Autowired
    private IMedicalHospitalService hospitalService;
    @Autowired
    private IMedicalDepartmentService departmentService;
    @Autowired
    private IMedicalExpertService expertService;

    @GetMapping("/medical/hospitals")
    @Operation(summary = "获取医院列表（患者端）")
    public R<IPage<MedicalHospital>> getHospitals(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalHospital> wrapper = new LambdaQueryWrapper<MedicalHospital>()
                .eq(MedicalHospital::getStatus, (byte) 1)
                .eq(StringUtils.hasText(city), MedicalHospital::getCity, city)
                .eq(StringUtils.hasText(level), MedicalHospital::getHospitalLevel, level)
                .and(StringUtils.hasText(keyword), w -> w
                        .like(MedicalHospital::getHospitalName, keyword)
                        .or().like(MedicalHospital::getIntroduction, keyword))
                .orderByAsc(MedicalHospital::getSort);
        return R.success(hospitalService.page(new Page<>(page, pageSize), wrapper));
    }

    @GetMapping("/medical/hospitals/{hospitalId}")
    @Operation(summary = "获取医院详情")
    public R<MedicalHospital> getHospitalDetail(@PathVariable Long hospitalId) {
        return R.success(hospitalService.getHospitalDetail(hospitalId));
    }

    @GetMapping("/hospital/list-by-city")
    @Operation(summary = "按城市获取医院列表")
    public R<IPage<MedicalHospital>> listByCity(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String level,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalHospital> w = new LambdaQueryWrapper<MedicalHospital>()
                .eq(MedicalHospital::getStatus, (byte) 1)
                .eq(StringUtils.hasText(city), MedicalHospital::getCity, city)
                .eq(StringUtils.hasText(level), MedicalHospital::getHospitalLevel, level)
                .orderByAsc(MedicalHospital::getSort);
        return R.success(hospitalService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/hospital/search")
    @Operation(summary = "搜索医院")
    public R<IPage<MedicalHospital>> searchHospitals(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalHospital> w = new LambdaQueryWrapper<MedicalHospital>()
                .eq(MedicalHospital::getStatus, (byte) 1)
                .eq(StringUtils.hasText(city), MedicalHospital::getCity, city)
                .and(StringUtils.hasText(keyword), q -> q
                        .like(MedicalHospital::getHospitalName, keyword)
                        .or().like(MedicalHospital::getAddress, keyword))
                .orderByAsc(MedicalHospital::getSort);
        return R.success(hospitalService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/hospital/popular")
    @Operation(summary = "获取热门医院")
    public R<List<MedicalHospital>> getPopularHospitals(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "10") Integer limit) {
        LambdaQueryWrapper<MedicalHospital> w = new LambdaQueryWrapper<MedicalHospital>()
                .eq(MedicalHospital::getStatus, (byte) 1)
                .eq(StringUtils.hasText(city), MedicalHospital::getCity, city)
                .orderByDesc(MedicalHospital::getCommentCount)
                .last("LIMIT " + limit);
        return R.success(hospitalService.list(w));
    }

    @GetMapping("/hospital/detail/{hospitalId}")
    @Operation(summary = "获取医院详情（别名路径）")
    public R<MedicalHospital> hospitalDetail(@PathVariable Long hospitalId) {
        return R.success(hospitalService.getHospitalDetail(hospitalId));
    }

    @GetMapping("/hospital/cities")
    @Operation(summary = "获取城市列表")
    public R<List<String>> getCities() {
        List<String> cities = hospitalService.listObjs(
                new LambdaQueryWrapper<MedicalHospital>()
                        .select(MedicalHospital::getCity)
                        .eq(MedicalHospital::getStatus, (byte) 1)
                        .groupBy(MedicalHospital::getCity)
                        .orderByAsc(MedicalHospital::getCity),
                obj -> (String) obj);
        return R.success(cities);
    }

    @GetMapping("/medical/departments")
    @Operation(summary = "获取科室列表")
    public R<IPage<MedicalDepartment>> getDepartments(
            @RequestParam(required = false) Long parentId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        LambdaQueryWrapper<MedicalDepartment> w = new LambdaQueryWrapper<MedicalDepartment>()
                .eq(MedicalDepartment::getStatus, (byte) 1)
                .eq(parentId != null, MedicalDepartment::getParentId, parentId)
                .orderByAsc(MedicalDepartment::getSort);
        return R.success(departmentService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/medical/departments/{departmentId}")
    @Operation(summary = "获取科室详情")
    public R<MedicalDepartment> getDepartmentDetail(@PathVariable Long departmentId) {
        return R.success(departmentService.getById(departmentId));
    }

    @GetMapping("/hospital/{hospitalId}/departments")
    @Operation(summary = "获取医院的科室列表")
    public R<List<MedicalDepartment>> getDeptsByHospital(
            @PathVariable Long hospitalId,
            @RequestParam(required = false) Long parentId) {
        // 科室表当前无hospital_id字段，返回全部启用科室（可后续扩展）
        LambdaQueryWrapper<MedicalDepartment> w = new LambdaQueryWrapper<MedicalDepartment>()
                .eq(MedicalDepartment::getStatus, (byte) 1)
                .eq(parentId != null, MedicalDepartment::getParentId, parentId)
                .orderByAsc(MedicalDepartment::getSort);
        return R.success(departmentService.list(w));
    }

    @GetMapping("/department/detail/{departmentId}")
    @Operation(summary = "获取科室详情（别名）")
    public R<MedicalDepartment> deptDetail(@PathVariable Long departmentId) {
        return R.success(departmentService.getById(departmentId));
    }

    @GetMapping("/department/search")
    @Operation(summary = "搜索科室")
    public R<List<MedicalDepartment>> searchDepartments(
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<MedicalDepartment> w = new LambdaQueryWrapper<MedicalDepartment>()
                .eq(MedicalDepartment::getStatus, (byte) 1)
                .and(StringUtils.hasText(keyword), q -> q
                        .like(MedicalDepartment::getDepartmentName, keyword)
                        .or().like(MedicalDepartment::getDescription, keyword))
                .orderByAsc(MedicalDepartment::getSort);
        return R.success(departmentService.list(w));
    }

    @GetMapping("/department/popular")
    @Operation(summary = "获取热门科室")
    public R<List<MedicalDepartment>> getPopularDepts(
            @RequestParam(defaultValue = "10") Integer limit) {
        LambdaQueryWrapper<MedicalDepartment> w = new LambdaQueryWrapper<MedicalDepartment>()
                .eq(MedicalDepartment::getStatus, (byte) 1)
                .isNull(MedicalDepartment::getParentId)
                .orderByAsc(MedicalDepartment::getSort)
                .last("LIMIT " + limit);
        return R.success(departmentService.list(w));
    }

    @GetMapping("/medical/experts")
    @Operation(summary = "获取专家列表")
    public R<IPage<MedicalExpert>> getExperts(
            @RequestParam(required = false) Long hospitalId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalExpert> w = new LambdaQueryWrapper<MedicalExpert>()
                .eq(MedicalExpert::getStatus, (byte) 1)
                .eq(hospitalId != null, MedicalExpert::getHospitalId, hospitalId)
                .eq(departmentId != null, MedicalExpert::getDepartmentId, departmentId)
                .and(StringUtils.hasText(keyword), q -> q
                        .like(MedicalExpert::getExpertName, keyword)
                        .or().like(MedicalExpert::getSpecialty, keyword))
                .orderByAsc(MedicalExpert::getSort);
        return R.success(expertService.page(new Page<>(page, pageSize), w));
    }

    @GetMapping("/medical/experts/{expertId}")
    @Operation(summary = "获取专家详情")
    public R<MedicalExpert> getExpertDetail(@PathVariable Long expertId) {
        return R.success(expertService.getExpertDetail(expertId));
    }

    @GetMapping("/department/{departmentId}/doctors")
    @Operation(summary = "获取科室专家列表")
    public R<IPage<MedicalExpert>> getDeptDoctors(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MedicalExpert> w = new LambdaQueryWrapper<MedicalExpert>()
                .eq(MedicalExpert::getStatus, (byte) 1)
                .eq(MedicalExpert::getDepartmentId, departmentId)
                .orderByAsc(MedicalExpert::getSort);
        return R.success(expertService.page(new Page<>(page, pageSize), w));
    }
}
