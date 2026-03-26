package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 陪诊师列表 VO（user_accompanist LEFT JOIN user_user 多表联查）
 *
 * @author Alanine
 */
@Data
@Schema(description = "陪诊师列表返回数据")
public class AccompanistListVO {

    @Schema(description = "陪诊师资质表ID")
    private Long id;

    @Schema(description = "关联用户ID")
    private Long userId;

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

    @Schema(description = "语言能力")
    private String languageAbility;

    @Schema(description = "专业擅长")
    private String specialties;

    @Schema(description = "已服务次数")
    private Integer serviceCount;

    @Schema(description = "评分")
    private BigDecimal ratingScore;

    @Schema(description = "准时率")
    private BigDecimal onTimeRate;

    @Schema(description = "投诉次数")
    private Integer complaintCount;

    @Schema(description = "陪诊状态 1空闲 2服务中 3休假 4停用")
    private Byte accompanyStatus;

    @Schema(description = "审核状态 1待审核 2已通过 3已拒绝")
    private Byte auditStatus;

    @Schema(description = "审核通过时间")
    private LocalDateTime auditTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 来自 user_user 表（多表联查） */
    @Schema(description = "账号状态（来自user_user）")
    private Byte userStatus;

    @Schema(description = "用户名（来自user_user）")
    private String username;

    @Schema(description = "头像URL（来自user_user）")
    private String avatarUrl;
}
