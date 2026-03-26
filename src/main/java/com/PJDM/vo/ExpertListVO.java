package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "专家列表VO")
public class ExpertListVO {
    private Long id;
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
    private Integer experienceYears;
    private BigDecimal appointmentFee;
    private Byte isAcceptInternational;
    private Byte isRecommend;
    private BigDecimal ratingScore;
    private Integer commentCount;
    private Byte status;
    private Integer sort;
    private LocalDateTime createTime;
}
