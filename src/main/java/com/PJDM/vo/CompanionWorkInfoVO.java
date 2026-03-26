package com.PJDM.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 陪诊师工作统计 VO
 */
@Data
public class CompanionWorkInfoVO {
    /** 陪诊师ID */
    private Long accompanistId;
    /** 真实姓名 */
    private String realName;
    /** 当前状态（1空闲 2服务中 3休假 4停用） */
    private Byte accompanyStatus;
    /** 审核状态（1待审核 2已通过 3已拒绝） */
    private Byte auditStatus;
    /** 已完成服务次数 */
    private Integer serviceCount;
    /** 综合评分 */
    private BigDecimal ratingScore;
    /** 准时率 */
    private BigDecimal onTimeRate;
    /** 投诉次数 */
    private Integer complaintCount;
    /** 进行中订单数 */
    private Long activeOrderCount;
    /** 待接订单数 */
    private Long pendingOrderCount;
}
