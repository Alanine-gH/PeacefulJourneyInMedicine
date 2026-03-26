package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.AuditDTO;
import com.PJDM.dto.IdentityQueryDTO;
import com.PJDM.pojo.UserIdentityAuth;
import com.PJDM.service.IUserIdentityAuthService;
import com.PJDM.vo.IdentityListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实名认证管理 Controller
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/user/identity")
@Tag(name = "实名认证管理", description = "实名认证查询、审核、删除")
public class UserIdentityAuthController {

    @Autowired
    private IUserIdentityAuthService identityAuthService;

    @GetMapping("/list")
    @Operation(summary = "分页查询实名认证列表（user_identity_auth JOIN user_user，证件号脱敏）")
    public R<IPage<IdentityListVO>> list(IdentityQueryDTO query) {
        return R.success(identityAuthService.getIdentityListPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取认证详情（明文，仅管理员可用）")
    public R<UserIdentityAuth> detail(@PathVariable Long id) {
        try {
            return R.success(identityAuthService.getIdentityDetail(id));
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/audit")
    @Operation(summary = "审核实名认证（status: 2通过 3拒绝）")
    public R<String> audit(@PathVariable Long id, @RequestBody AuditDTO dto) {
        try {
            identityAuthService.auditIdentity(id, dto);
            return R.success("审核成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除认证记录")
    public R<String> delete(@PathVariable Long id) {
        try {
            identityAuthService.deleteIdentity(id);
            return R.success("删除成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
