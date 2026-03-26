package com.PJDM.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 就诊人新增/修改 DTO
 */
@Data
public class PatientUpdateDTO {
    /** 所属用户ID（新增时必填） */
    private Long userId;
    /** 姓名 */
    private String name;
    /** 手机号 */
    private String phone;
    /** 身份证号 */
    private String idCard;
    /** 性别（0：未知；1：男；2：女） */
    private Byte gender;
    /** 出生日期 */
    private LocalDate birthday;
    /** 与账户人关系 */
    private String relation;
    /** 地址 */
    private String address;
    /** 备注 */
    private String remark;
}
