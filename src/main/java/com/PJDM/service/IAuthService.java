package com.PJDM.service;

import com.PJDM.dto.LoginDTO;
import com.PJDM.dto.RegisterDTO;
import com.PJDM.pojo.UserUser;
import com.PJDM.vo.LoginVO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户认证 Service 接口
 *
 * @author Alanine
 */
public interface IAuthService extends IService<UserUser> {

    /**
     * 用户登录
     *
     * @param loginDTO 登录请求
     * @param request  HTTP 请求（用于提取客户端 IP、UA 等信息存入 Token）
     * @return 登录结果（含 Token）
     */
    LoginVO login(LoginDTO loginDTO, HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param registerDTO 注册请求
     */
    void register(RegisterDTO registerDTO);

    /**
     * 退出登录（使 Token 失效）
     *
     * @param token JWT Token
     */
    void logout(String token);

    /**
     * 重置密码（忘记密码）
     * 校验用户名与手机号同时匹配，且图形验证码正确，方可修改密码
     *
     * @param username   用户名
     * @param phone      手机号
     * @param newPassword 新密码
     * @param captcha    图形验证码
     * @param captchaKey 验证码UUID
     */
    void resetPassword(String username, String phone, String newPassword, String captcha, String captchaKey);
}
