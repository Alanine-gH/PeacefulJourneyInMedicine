package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 陪诊师新增/修改参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "陪诊师新增/修改参数")
public class AccompanistUpdateDTO {

    @Schema(description = "关联用户ID（已有账号时填写，与username/password二选一）")
    private Long userId;

    @Schema(description = "新建账号用户名（新增时若不填userId则必填）")
    private String username;

    @Schema(description = "新建账号密码（新增时若不填userId则必填）")
    private String password;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别 1男 2女 3未知")
    private Byte gender;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "职称")
    private String professionalTitle;

    @Schema(description = "医学背景 1有 2无")
    private Byte medicalBackground;

    @Schema(description = "证书编号")
    private String certificateNumber;

    @Schema(description = "语言能力")
    private String languageAbility;

    @Schema(description = "专业擅长")
    private String specialties;

    @Schema(description = "陪诊状态 1空闲 2服务中 3休假 4停用")
    private Byte accompanyStatus;

    @Schema(description = "证书照片URL")
    private String certificatePhoto;

    @Schema(description = "备注")
    private String remark;
}
