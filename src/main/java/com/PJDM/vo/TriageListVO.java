package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "分诊记录列表VO")
public class TriageListVO {
    private Long id;
    private String triageNo;
    private Long userId;
    private String userName;
    private String userPhone;
    private Byte gender;
    private Integer age;
    private String chiefComplaint;
    private String painLocation;
    private Byte painLevel;
    private String recommendedDepartment;
    private String recommendedHospitals;
    private Byte triageStatus;
    private BigDecimal aiConfidence;
    private LocalDateTime aiAnalysisTime;
    private LocalDateTime humanReviewTime;
    private LocalDateTime createTime;
}
