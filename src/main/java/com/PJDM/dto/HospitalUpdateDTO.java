package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "医院新增/修改参数")
public class HospitalUpdateDTO {
    private String hospitalCode;
    private String hospitalName;
    private String hospitalNameEn;
    private Byte hospitalType;
    private String hospitalLevel;
    private String province;
    private String city;
    private String district;
    private String address;
    private String phone;
    private String internationalPhone;
    private String website;
    private String introduction;
    private String featuredDepartments;
    private String appointmentNotes;
    private Byte hasInternationalDept;
    private Byte isCooperation;
    private LocalDate cooperationStartDate;
    private String logoUrl;
    private String coverImageUrl;
    private Byte status;
    private Integer sort;
    private String remark;
}
