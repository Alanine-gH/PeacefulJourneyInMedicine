package com.PJDM.dto;

import lombok.Data;

/**
 * 陪诊师日程更新 DTO
 */
@Data
public class CompanionScheduleDTO {
    /** 日程状态（1：可用；2：已预约；3：已占用；4：请假） */
    private Byte scheduleStatus;
    /** 备注 */
    private String remark;
}
