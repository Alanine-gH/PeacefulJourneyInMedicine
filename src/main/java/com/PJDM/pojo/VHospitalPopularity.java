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
@TableName("v_hospital_popularity")
public class VHospitalPopularity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    private Long id;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 城市
     */
    private String city;

    private Long appointmentCount;

    private Long orderCount;

    private BigDecimal avgRating;

    /**
     * 评论数
     */
    private Integer commentCount;
}
