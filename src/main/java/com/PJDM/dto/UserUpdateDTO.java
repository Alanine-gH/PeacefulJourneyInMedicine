package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户新增/修改参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "用户新增/修改参数")
public class UserUpdateDTO {

    @Schema(description = "用户名（新增必填）")
    private String username;

    @Schema(description = "密码（新增必填，修改时不传则不改）")
    private String password;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户类型 1患者 2陪诊师 3客服 4管理员")
    private Byte userType;

    @Schema(description = "账号状态 1活跃 2停用 3禁用")
    private Byte status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "性别 0未知 1男 2女")
    private Byte gender;

    @Schema(description = "国家代码（如+86、+1）")
    private String countryCode;
}
