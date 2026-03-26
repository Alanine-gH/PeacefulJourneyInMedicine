package com.PJDM.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 就诊人详情 VO
 */
@Data
public class PatientDetailVO {
    private Long id;
    private Long userId;
    private String name;
    private String phone;
    private String idCard;
    private Byte gender;
    private LocalDate birthday;
    private String relation;
    private String address;
    private Byte isDefault;
    private LocalDateTime createTime;
    /** 就诊卡列表 */
    private List<MedicalCardVO> medicalCards;

    @Data
    public static class MedicalCardVO {
        private Long id;
        private Long hospitalId;
        private String hospitalName;
        private String cardNo;
        private LocalDateTime createTime;
    }
}
