package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单评价DTO
 *
 * @author Alanine
 */
@Data
@Schema(description = "订单评价请求参数")
public class OrderEvaluationDTO {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "评分 (1-5分)")
    private Integer ratingScore;

    @Schema(description = "评价标签，多个标签用逗号分隔")
    private String ratingLabels;

    @Schema(description = "评价内容")
    private String evaluationContent;

    @Schema(description = "评价图片，多个图片URL用逗号分隔")
    private String evaluationImages;
}
