package com.PJDM.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 发布需求 DTO
 */
@Data
public class DemandPublishDTO {
    /** 需求标题 */
    private String title;
    /** 需求描述 */
    private String description;
    /** 需求类型（accompany：陪护；nursing：护理；other：其他） */
    private String type;
    /** 医院ID */
    private Long hospitalId;
    /** 科室ID */
    private Long departmentId;
    /** 就诊人ID */
    private Long patientId;
    /** 开始日期 */
    private LocalDate startDate;
    /** 结束日期 */
    private LocalDate endDate;
    /** 预算金额 */
    private BigDecimal budget;
    /** 联系人姓名 */
    private String contactName;
    /** 联系人电话 */
    private String contactPhone;
    /** 特殊要求 */
    private String requirements;
    /** 城市 */
    private String city;
    /** 备注 */
    private String remark;
}
