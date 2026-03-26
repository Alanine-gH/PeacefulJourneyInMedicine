package com.PJDM.dto;

import lombok.Data;

/**
 * 增值服务选择 DTO
 */
@Data
public class ValueAddedSelectDTO {
    /** 增值服务ID（service_package.id，packageType=2增值套餐） */
    private Long serviceId;
    /** 关联订单号 */
    private String orderNo;
    /** 数量 */
    private Integer quantity;
}
