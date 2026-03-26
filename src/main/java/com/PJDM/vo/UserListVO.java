package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户列表 VO（user_user LEFT JOIN user_identity_auth 多表联查）
 *
 * @author Alanine
 */
@Data
@Schema(description = "用户列表返回数据")
public class UserListVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "性别 0未知 1男 2女")
    private Byte gender;

    @Schema(description = "用户类型 1患者 2陪诊师 3客服 4管理员")
    private Byte userType;

    @Schema(description = "账号状态 1活跃 2停用 3禁用")
    private Byte status;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 来自 user_identity_auth 表（多表联查） */
    @Schema(description = "实名认证状态 1待审核 2已通过 3已拒绝，null表示未提交")
    private Byte authStatus;

    @Schema(description = "认证类型 1护照 2身份证 3其他")
    private Byte authType;
}
