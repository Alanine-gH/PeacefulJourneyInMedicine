package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 实名认证查询参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "实名认证查询参数")
public class IdentityQueryDTO {

    @Schema(description = "真实姓名（模糊）")
    private String realName;

    @Schema(description = "认证状态 1待审核 2已通过 3已拒绝")
    private Byte authStatus;

    @Schema(description = "认证类型 1护照 2身份证 3其他")
    private Byte authType;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
