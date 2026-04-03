package com.PJDM.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PackageListVO {
        private Long id;
        private String packageCode;
        private String packageName;
        private String packageNameEn;
        private Byte packageType;
        private Byte serviceType;
        private BigDecimal price;
        private String currency;
        private String unit;
        private Integer duration;
        private Integer maxUsageCount;
        private Integer validDays;
        private String coverImageUrl;
        private String includesItems;
        private Integer sort;
        private Byte status;
        private LocalDateTime createTime;
}
