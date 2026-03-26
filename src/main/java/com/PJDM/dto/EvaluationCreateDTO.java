package com.PJDM.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 患者端创建评价 DTO
 */
@Data
public class EvaluationCreateDTO {
    /** 订单号 */
    private String orderNo;
    /** 评分（1-5） */
    private BigDecimal ratingScore;
    /** 评价标签列表 */
    private List<String> ratingLabels;
    /** 评价内容 */
    private String evaluationContent;
    /** 评价图片（逗号分隔URL） */
    private String evaluationImages;
}
