package com.PJDM.dto;

import lombok.Data;

/**
 * 收藏操作 DTO
 */
@Data
public class PatientFavoriteDTO {
    /** 收藏类型（companion/package/hospital/expert） */
    private String targetType;
    /** 收藏目标ID */
    private Long targetId;
}
