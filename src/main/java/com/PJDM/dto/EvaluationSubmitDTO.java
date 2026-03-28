package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 患者提交评价 DTO
 */
@Data
@Schema(description = "提交评价参数")
public class EvaluationSubmitDTO {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "评分（1-5）")
    private BigDecimal ratingScore;

    @Schema(description = "评价内容")
    private String evaluationContent;

    @Schema(description = "评价标签（逗号分隔）")
    private String ratingLabels;

    @Schema(description = "是否匿名")
    private Boolean anonymous;
}
