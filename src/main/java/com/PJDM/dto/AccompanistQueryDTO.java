package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 陪诊师列表查询参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "陪诊师查询参数")
public class AccompanistQueryDTO {

    @Schema(description = "姓名或手机号（模糊）")
    private String realName;

    @Schema(description = "审核状态 1待审核 2已通过 3已拒绝")
    private Byte auditStatus;

    @Schema(description = "陪诊状态 1空闲 2服务中 3休假 4停用")
    private Byte accompanyStatus;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
