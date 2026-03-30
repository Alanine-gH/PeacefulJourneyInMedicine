package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.LoginDTO;
import com.PJDM.dto.RegisterDTO;
import com.PJDM.service.IAuthService;
import com.PJDM.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器（登录 / 注册 / 退出）
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "认证接口", description = "登录、注册、退出")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private SMSSendCode smsSendCode;

    /**
     * 登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public R<LoginVO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            LoginVO vo = authService.login(loginDTO, request);
            return R.success(vo);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 注册（患者/陪诊师）
     * userType=1 → 患者，仅插 user_user
     * userType=2 → 陪诊师，插 user_user + user_accompanist（待审核）
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册（患者或陪诊师）")
    public R<String> register(@RequestBody RegisterDTO registerDTO) {
        try {
            byte userType = (registerDTO.getUserType() != null && registerDTO.getUserType() == 2) ? (byte) 2 : (byte) 1;
            log.info("[注册] username={} userType={}", registerDTO.getUsername(), userType);
            authService.register(registerDTO);
            String msg = userType == 2 ? "注册成功，您的陪诊师账号需等待管理员审核后方可接单" : "注册成功";
            return R.success(msg);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 重置密码（忘记密码）
     */
    @PostMapping("/reset-password")
    @Operation(summary = "重置密码")
    public R<String> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String phone = body.get("phone");
            String newPassword = body.get("newPassword");
            String captcha = body.get("captcha");
            String captchaKey = body.get("captchaKey");
            if (!StringUtils.hasText(username)) return R.error("用户名不能为空");
            if (!StringUtils.hasText(phone)) return R.error("手机号不能为空");
            if (!StringUtils.hasText(newPassword)) return R.error("新密码不能为空");
            authService.resetPassword(username, phone, newPassword, captcha, captchaKey);
            return R.success("密码重置成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public R<String> logout(HttpServletRequest request) {
        String token = extractToken(request);
        authService.logout(token);
        return R.success("退出成功");
    }

    /**
     * 从请求头中提取 Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return bearer;
    }

    /**
     * 发送短信验证码
     * 前端以 JSON 格式传参：{"phone": "13800138000"}
     */
    @PostMapping("/send-code")
    public R sendCode(@RequestBody Map<String, String> body) throws Exception {
        String phone = body.get("phone");
        if (!StringUtils.hasText(phone)) {
            return R.error("手机号不能为空");
        }
        int code = (int) (Math.random() * 900000) + 100000;
        smsSendCode.sendCode(phone, String.valueOf(code));
        System.out.println("phone=" + phone + ",code=" + code);
        log.info("[发送验证码] phone={}, code={}", phone, code);
        return R.success("验证码已发送");
    }
}
