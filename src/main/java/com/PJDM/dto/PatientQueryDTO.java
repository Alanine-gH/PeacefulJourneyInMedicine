package com.PJDM.dto;

import lombok.Data;

/**
 * 就诊人查询 DTO
 */
@Data
public class PatientQueryDTO {
    /** 所属用户ID */
    private Long userId;
    /** 姓名（模糊搜索） */
    private String name;
    /** 页码 */
    private Integer pageNum;
    /** 每页数量 */
    private Integer pageSize;
}
