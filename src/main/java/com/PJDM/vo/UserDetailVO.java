package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情 VO（含角色信息）
 *
 * @author Alanine
 */
@Data
@Schema(description = "用户详情返回数据")
public class UserDetailVO {

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

    @Schema(description = "用户类型")
    private Byte userType;

    @Schema(description = "账号状态")
    private Byte status;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "角色列表（来自 user_role 多表联查）")
    private List<RoleSimpleVO> roles;

    @Data
    @Schema(description = "角色简要信息")
    public static class RoleSimpleVO {
        @Schema(description = "角色ID")
        private Long id;
        @Schema(description = "角色名称")
        private String roleName;
        @Schema(description = "角色编码")
        private String roleCode;
    }
}
