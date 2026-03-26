package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审核操作参数（陪诊师审核 / 实名认证审核通用）
 *
 * @author Alanine
 */
@Data
@Schema(description = "审核参数")
public class AuditDTO {

    @Schema(description = "审核结果状态（2：通过；3：拒绝）", required = true)
    private Byte status;

    @Schema(description = "拒绝原因（拒绝时必填）")
    private String failReason;
}
