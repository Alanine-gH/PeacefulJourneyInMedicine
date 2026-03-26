package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.RealNameAuthDTO;
import com.PJDM.pojo.UserIdentityAuth;
import com.PJDM.service.IUserIdentityAuthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 患者端 - 实名认证接口
 */
@Slf4j
@RestController
@Tag(name = "患者端-实名认证", description = "实名认证提交与查询")
public class AuthRealNameController {

    @Autowired private IUserIdentityAuthService identityAuthService;

    @PostMapping("/auth/real-name")
    @Operation(summary = "提交实名认证")
    public R<String> submitRealNameAuth(@RequestParam Long userId, @RequestBody RealNameAuthDTO dto) {
        log.info("[实名认证] 提交 userId={}", userId);
        UserIdentityAuth auth = buildAuth(userId, dto);
        identityAuthService.save(auth);
        return R.success("提交成功，等待审核");
    }

    @GetMapping("/auth/real-name/status")
    @Operation(summary = "获取实名认证状态")
    public R<Map<String, Object>> getAuthStatus(@RequestParam Long userId) {
        UserIdentityAuth auth = latestAuth(userId);
        if (auth == null) return R.success(Map.of("authStatus", 0, "message", "未提交认证"));
        return R.success(Map.of(
                "authStatus", auth.getAuthStatus(),
                "authFailReason", auth.getAuthFailReason() != null ? auth.getAuthFailReason() : "",
                "authTime", auth.getAuthTime() != null ? auth.getAuthTime().toString() : ""
        ));
    }

    @GetMapping("/auth/real-name/info")
    @Operation(summary = "获取实名认证信息")
    public R<UserIdentityAuth> getAuthInfo(@RequestParam Long userId) {
        return R.success(latestAuth(userId));
    }

    @PostMapping("/auth/real-name/resubmit")
    @Operation(summary = "重新提交实名认证")
    public R<String> resubmit(@RequestParam Long userId, @RequestBody RealNameAuthDTO dto) {
        identityAuthService.remove(new LambdaQueryWrapper<UserIdentityAuth>()
                .eq(UserIdentityAuth::getUserId, userId));
        identityAuthService.save(buildAuth(userId, dto));
        return R.success("重新提交成功，等待审核");
    }

    @PostMapping("/auth/real-name/cancel")
    @Operation(summary = "取消实名认证申请")
    public R<String> cancelAuth(@RequestParam Long userId) {
        identityAuthService.remove(new LambdaQueryWrapper<UserIdentityAuth>()
                .eq(UserIdentityAuth::getUserId, userId)
                .eq(UserIdentityAuth::getAuthStatus, (byte) 1));
        return R.success("已取消");
    }

    @PostMapping("/auth/real-name/step1")
    @Operation(summary = "保存认证步骤1（姓名+身份证号）")
    public R<String> saveStep1(@RequestParam Long userId, @RequestBody RealNameAuthDTO dto) {
        return R.success("步骤1数据已暂存，请继续下一步");
    }

    @PostMapping("/auth/real-name/step2")
    @Operation(summary = "保存认证步骤2（身份证照片）")
    public R<String> saveStep2(@RequestParam Long userId, @RequestBody RealNameAuthDTO dto) {
        return R.success("步骤2数据已暂存，请继续下一步");
    }

    @PostMapping("/auth/real-name/step3")
    @Operation(summary = "保存认证步骤3（人脸识别照片）")
    public R<String> saveStep3(@RequestParam Long userId, @RequestBody RealNameAuthDTO dto) {
        return R.success("步骤3数据已暂存，请提交认证");
    }

    @GetMapping("/auth/real-name/step/{step}")
    @Operation(summary = "获取认证步骤数据")
    public R<UserIdentityAuth> getStepData(@PathVariable Integer step, @RequestParam Long userId) {
        return R.success(latestAuth(userId));
    }

    // ==================== 私有方法 ====================

    private UserIdentityAuth latestAuth(Long userId) {
        return identityAuthService.getOne(new LambdaQueryWrapper<UserIdentityAuth>()
                .eq(UserIdentityAuth::getUserId, userId)
                .orderByDesc(UserIdentityAuth::getId)
                .last("LIMIT 1"));
    }

    private UserIdentityAuth buildAuth(Long userId, RealNameAuthDTO dto) {
        UserIdentityAuth auth = new UserIdentityAuth();
        auth.setUserId(userId);
        auth.setRealName(dto.getRealName());
        auth.setIdCardNumber(dto.getIdCard());
        auth.setAuthType(dto.getAuthType() != null ? dto.getAuthType() : (byte) 2);
        auth.setIdCardPhotoFront(dto.getIdCardFront());
        auth.setIdCardPhotoBack(dto.getIdCardBack());
        auth.setPassportNumber(dto.getPassportNumber());
        auth.setPassportPhotoFront(dto.getPassportPhotoFront());
        auth.setPassportPhotoBack(dto.getPassportPhotoBack());
        auth.setAuthStatus((byte) 1);
        auth.setCreateTime(LocalDateTime.now());
        return auth;
    }
}
