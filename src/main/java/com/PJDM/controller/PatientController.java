package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.PatientFavoriteDTO;
import com.PJDM.dto.PatientMedicalCardDTO;
import com.PJDM.dto.PatientQueryDTO;
import com.PJDM.dto.PatientUpdateDTO;
import com.PJDM.pojo.UserFavorite;
import com.PJDM.service.IUserFavoriteService;
import com.PJDM.service.IUserPatientService;
import com.PJDM.service.IUserUserService;
import com.PJDM.vo.PatientDetailVO;
import com.PJDM.vo.PatientProfileVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 患者端 - 个人中心 / 就诊人 / 收藏
 */
@Slf4j
@RestController
@Tag(name = "患者端", description = "患者个人中心、就诊人管理、收藏管理")
public class PatientController {

    @Autowired
    private IUserPatientService patientService;
    @Autowired
    private IUserFavoriteService favoriteService;
    @Autowired
    private IUserUserService userUserService;

    // ==================== 患者个人信息 ====================

    @GetMapping("/patient/profile")
    @Operation(summary = "获取患者个人信息")
    public R<PatientProfileVO> getProfile(@RequestParam Long userId) {
        var user = userUserService.getById(userId);
        if (user == null) return R.error("用户不存在");
        PatientProfileVO vo = new PatientProfileVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickName());
        vo.setRealName(user.getRealName());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setAge(user.getAge());
        vo.setGender(user.getGender());
        vo.setLanguage(user.getLanguage());
        vo.setStatus(user.getStatus());
        vo.setLastLoginTime(user.getLastLoginTime());
        vo.setCreateTime(user.getCreateTime());
        return R.success(vo);
    }

    // ==================== 就诊人管理 ====================

    @GetMapping("/user/patient/list")
    @Operation(summary = "获取就诊人列表")
    public R<IPage<PatientDetailVO>> getPatientList(PatientQueryDTO query) {
        log.info("[就诊人] 列表 userId={}", query.getUserId());
        return R.success(patientService.getPatientPage(query));
    }

    @GetMapping("/user/patient/{patientId}")
    @Operation(summary = "获取就诊人详情")
    public R<PatientDetailVO> getPatientDetail(@PathVariable Long patientId) {
        return R.success(patientService.getPatientDetail(patientId));
    }

    @PostMapping("/user/patient")
    @Operation(summary = "添加就诊人")
    public R<String> addPatient(@RequestBody PatientUpdateDTO dto) {
        patientService.addPatient(dto);
        return R.success("添加成功");
    }

    @PutMapping("/user/patient/{patientId}")
    @Operation(summary = "更新就诊人信息")
    public R<String> updatePatient(@PathVariable Long patientId, @RequestBody PatientUpdateDTO dto) {
        patientService.updatePatient(patientId, dto);
        return R.success("更新成功");
    }

    @DeleteMapping("/user/patient/{patientId}")
    @Operation(summary = "删除就诊人")
    public R<String> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return R.success("删除成功");
    }

    @PostMapping("/user/patient/{patientId}/default")
    @Operation(summary = "设置默认就诊人")
    public R<String> setDefault(@PathVariable Long patientId, @RequestParam Long userId) {
        patientService.setDefaultPatient(userId, patientId);
        return R.success("设置成功");
    }

    @PostMapping("/user/patient/{patientId}/medical-card")
    @Operation(summary = "添加就诊卡")
    public R<String> addMedicalCard(@PathVariable Long patientId,
                                    @RequestBody PatientMedicalCardDTO dto) {
        patientService.addMedicalCard(patientId, dto);
        return R.success("添加成功");
    }

    @DeleteMapping("/user/patient/{patientId}/medical-card/{cardId}")
    @Operation(summary = "删除就诊卡")
    public R<String> deleteMedicalCard(@PathVariable Long patientId, @PathVariable Long cardId) {
        patientService.deleteMedicalCard(patientId, cardId);
        return R.success("删除成功");
    }

    // ==================== 收藏管理 ====================

    @GetMapping("/patient/favorites")
    @Operation(summary = "获取收藏列表")
    public R<IPage<UserFavorite>> getFavorites(
            @RequestParam Long userId,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return R.success(favoriteService.getFavoritePage(userId, targetType, page, pageSize));
    }

    @PostMapping("/patient/favorites")
    @Operation(summary = "添加收藏")
    public R<String> addFavorite(@RequestParam Long userId, @RequestBody PatientFavoriteDTO dto) {
        favoriteService.addFavorite(userId, dto);
        return R.success("收藏成功");
    }

    @PostMapping("/patient/favorites/remove")
    @Operation(summary = "取消收藏")
    public R<String> removeFavorite(@RequestParam Long userId, @RequestBody PatientFavoriteDTO dto) {
        favoriteService.removeFavorite(userId, dto);
        return R.success("取消收藏成功");
    }
}
