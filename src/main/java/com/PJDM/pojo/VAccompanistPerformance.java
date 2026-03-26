package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("v_accompanist_performance")
public class VAccompanistPerformance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    private Long id;

    /**
     * 真实姓名（冗余user_user.real_name）
     */
    private String realName;

    /**
     * 手机号（冗余user_user.phone）
     */
    private String phone;

    /**
     * 已服务次数
     */
    private Integer serviceCount;

    /**
     * 评分（如4.85）
     */
    private BigDecimal ratingScore;

    /**
     * 准时率（如98.50）
     */
    private BigDecimal onTimeRate;

    /**
     * 投诉次数
     */
    private Integer complaintCount;

    private Long recentOrders;

    private BigDecimal recentAvgRating;
}
