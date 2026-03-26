package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "专家新增/修改参数")
public class ExpertUpdateDTO {
    @Schema(description = "新增时必填：用户名（用于创建user_user账号）")
    private String username;
    @Schema(description = "新增时必填：密码")
    private String password;
    private String expertCode;
    private String expertName;
    private String expertNameEn;
    private Long hospitalId;
    private String hospitalName;
    private Long departmentId;
    private String departmentName;
    private String professionalTitle;
    private String specialty;
    private String languageAbility;
    private Byte gender;
    private Integer age;
    private String photoUrl;
    private String introduction;
    private Integer experienceYears;
    private String educationBackground;
    private BigDecimal appointmentFee;
    private Byte isAcceptInternational;
    private Byte isRecommend;
    private Byte status;
    private Integer sort;
    private String remark;
}
