package com.PJDM.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 评价统计 VO
 */
@Data
public class EvaluationStatVO {
    /** 评价对象类型 */
    private String targetType;
    /** 评价对象ID */
    private Long targetId;
    /** 平均评分 */
    private BigDecimal avgScore;
    /** 总评价数 */
    private Long totalCount;
    /** 各评分数量（key=评分，value=数量） */
    private Map<Integer, Long> scoreDistribution;
}
