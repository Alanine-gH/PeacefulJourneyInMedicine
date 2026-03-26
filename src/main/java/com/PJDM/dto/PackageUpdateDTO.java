package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "服务套餐新增/修改参数")
public class PackageUpdateDTO {
    private String packageCode;
    private String packageName;
    private String packageNameEn;
    private Byte packageType;
    private Byte serviceType;
    private String description;
    private String serviceContent;
    private BigDecimal price;
    private String currency;
    private String unit;
    private Integer duration;
    private Integer maxUsageCount;
    private Integer validDays;
    private String coverImageUrl;
    private String includesItems;
    private String excludeItems;
    private String usageNotice;
    private Integer sort;
    private Byte status;
    private String remark;
}
