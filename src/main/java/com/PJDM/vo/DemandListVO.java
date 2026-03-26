package com.PJDM.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 需求列表 VO
 */
@Data
public class DemandListVO {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String description;
    private String type;
    private String hospitalName;
    private String departmentName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private String city;
    private String contactName;
    private String contactPhone;
    private String requirements;
    /** 状态（1：待接单；2：已接单；3：已完成；4：已取消） */
    private Byte status;
    private LocalDateTime refreshTime;
    private LocalDateTime createTime;
}
