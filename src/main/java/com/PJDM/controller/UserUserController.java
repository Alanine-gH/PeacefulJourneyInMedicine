package com.PJDM.controller;

import com.PJDM.common.BaseContext;
import com.PJDM.common.R;
import com.PJDM.dto.UserQueryDTO;
import com.PJDM.dto.UserUpdateDTO;
import com.PJDM.service.IUserUserService;
import com.PJDM.vo.UserDetailVO;
import com.PJDM.vo.UserListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理 Controller
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户增删改查、状态管理、密码重置")
public class UserUserController {

    @Autowired
    private IUserUserService userUserService;

    @GetMapping("/stat/count")
    @Operation(summary = "查询用户总数")
    public R<Long> count() {
        return R.success(userUserService.count());
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询用户列表（含认证状态多表联查）")
    public R<IPage<UserListVO>> list(UserQueryDTO query) {
        return R.success(userUserService.getUserListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情（含角色）")
    public R<UserDetailVO> detail(@PathVariable Long id) {
        try {
            return R.success(userUserService.getUserDetail(id));
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public R<String> add(@RequestBody UserUpdateDTO dto) {
        try {
            userUserService.addUser(dto);
            return R.success("新增成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改用户")
    public R<String> update(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        try {
            userUserService.updateUser(id, dto);
            return R.success("修改成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public R<String> delete(@PathVariable Long id) {
        try {
            userUserService.deleteUser(id);
            return R.success("删除成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "修改账号状态（1活跃 2停用 3禁用）")
    public R<String> updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Byte> body) {
        try {
            Byte status = body.get("status");
            if (status == null) return R.error("status 不能为空");
            userUserService.updateStatus(id, status);
            return R.success("状态修改成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "重置密码")
    public R<String> resetPassword(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        try {
            String newPassword = body.get("newPassword");
            if (!org.springframework.util.StringUtils.hasText(newPassword)) return R.error("新密码不能为空");
            userUserService.resetPassword(id, newPassword);
            return R.success("密码重置成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 患者端API - 获取当前用户个人资料
     */
    @GetMapping("/patient/profile")
    @Operation(summary = "获取患者个人资料")
    public R<UserDetailVO> getPatientProfile(@RequestHeader("Authorization") String token) {
        try {
            // 从token中获取userId（简化处理，实际应该从BaseContext获取）
            Long userId = getUserIdFromToken(token);
            return R.success(userUserService.getUserDetail(userId));
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 患者端API - 更新当前用户个人资料
     */
    @PutMapping("/patient/profile")
    @Operation(summary = "更新患者个人资料")
    public R<String> updatePatientProfile(@RequestHeader("Authorization") String token, @RequestBody UserUpdateDTO dto) {
        try {
            Long userId = getUserIdFromToken(token);
            userUserService.updateUser(userId, dto);
            return R.success("更新成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 从token中提取userId（简化实现）
     */
    private Long getUserIdFromToken(String token) {
        // 这里简化处理，实际应该从BaseContext.getUserId()获取
        // 暂时返回固定值用于测试
        return BaseContext.getUserId();
    }

}
