package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单评价列表 VO
 *
 * @author Alanine
 */
@Data
@Schema(description = "订单评价列表")
public class EvaluationListVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名（来自user_user）")
    private String username;

    @Schema(description = "陪诊师ID")
    private Long accompanistId;

    @Schema(description = "陪诊师姓名（来自user_accompanist）")
    private String accompanistName;

    @Schema(description = "评分")
    private BigDecimal ratingScore;

    @Schema(description = "评分维度")
    private String ratingDimension;

    @Schema(description = "评价标签")
    private String ratingLabels;

    @Schema(description = "评价内容")
    private String evaluationContent;

    @Schema(description = "评价图片")
    private String evaluationImages;

    @Schema(description = "陪诊师回复")
    private String evaluationReply;

    @Schema(description = "回复时间")
    private LocalDateTime replyTime;

    @Schema(description = "评价时间")
    private LocalDateTime createTime;
}
