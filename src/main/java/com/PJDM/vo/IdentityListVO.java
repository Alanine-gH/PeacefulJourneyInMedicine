package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实名认证列表 VO（user_identity_auth LEFT JOIN user_user 多表联查）
 *
 * @author Alanine
 */
@Data
@Schema(description = "实名认证列表返回数据")
public class IdentityListVO {

    @Schema(description = "认证记录ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "认证类型 1护照 2身份证 3其他")
    private Byte authType;

    @Schema(description = "身份证号（脱敏）")
    private String idCardNumber;

    @Schema(description = "护照号（脱敏）")
    private String passportNumber;

    @Schema(description = "身份证正面URL")
    private String idCardPhotoFront;

    @Schema(description = "身份证背面URL")
    private String idCardPhotoBack;

    @Schema(description = "护照正面URL")
    private String passportPhotoFront;

    @Schema(description = "护照背面URL")
    private String passportPhotoBack;

    @Schema(description = "认证状态 1待审核 2已通过 3已拒绝")
    private Byte authStatus;

    @Schema(description = "拒绝原因")
    private String authFailReason;

    @Schema(description = "认证通过时间")
    private LocalDateTime authTime;

    @Schema(description = "提交时间")
    private LocalDateTime createTime;

    /** 来自 user_user 表（多表联查） */
    @Schema(description = "用户名（来自user_user）")
    private String username;

    @Schema(description = "手机号（来自user_user）")
    private String phone;
}
