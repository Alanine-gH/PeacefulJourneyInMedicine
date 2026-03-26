package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 评价列表查询参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "评价查询参数")
public class EvaluationQueryDTO {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "陪诊师ID")
    private Long accompanistId;

    @Schema(description = "最低评分（如3.0）")
    private Double minScore;

    @Schema(description = "最高评分（如5.0）")
    private Double maxScore;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
