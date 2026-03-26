package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "医院列表VO")
public class HospitalListVO {
    private Long id;
    private String hospitalCode;
    private String hospitalName;
    private String hospitalNameEn;
    private Byte hospitalType;
    private String hospitalLevel;
    private String province;
    private String city;
    private String address;
    private String phone;
    private String internationalPhone;
    private Byte hasInternationalDept;
    private Byte isCooperation;
    private LocalDate cooperationStartDate;
    private String logoUrl;
    private BigDecimal ratingScore;
    private Integer commentCount;
    private Byte status;
    private Integer sort;
    private String featuredDepartments;
    private LocalDateTime createTime;
}
