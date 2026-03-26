package com.PJDM.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 增值服务 VO（基于 service_package 表 packageType=2）
 */
@Data
public class ValueAddedServiceVO {
    private Long id;
    private String packageCode;
    private String packageName;
    private String packageNameEn;
    private Byte serviceType;
    private String description;
    private String serviceContent;
    private BigDecimal price;
    private String currency;
    private String unit;
    private Integer duration;
    private String coverImageUrl;
    private String includesItems;
    private String usageNotice;
    private Byte status;
    private Integer sort;
    private LocalDateTime createTime;
}
