package com.PJDM.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 需求查询 DTO
 */
@Data
public class DemandQueryDTO {
    /** 发布用户ID */
    private Long userId;
    /** 城市 */
    private String city;
    /** 需求类型 */
    private String type;
    /** 最低预算 */
    private BigDecimal minBudget;
    /** 最高预算 */
    private BigDecimal maxBudget;
    /** 开始日期 */
    private LocalDate startDate;
    /** 结束日期 */
    private LocalDate endDate;
    /** 状态 */
    private Byte status;
    /** 页码 */
    private Integer pageNum;
    /** 每页数量 */
    private Integer pageSize;
}
