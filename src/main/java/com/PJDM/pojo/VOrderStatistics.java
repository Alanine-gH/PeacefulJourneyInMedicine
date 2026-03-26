package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@TableName("v_order_statistics")
public class VOrderStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate orderDate;

    private Long totalOrders;

    private BigDecimal totalAmount;

    private Long completedOrders;

    private Long cancelledOrders;

    private BigDecimal avgRating;
}
