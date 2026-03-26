package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分诊记录查询参数")
public class TriageQueryDTO {
    @Schema(description = "患者姓名/手机（模糊）")
    private String userName;
    @Schema(description = "推荐科室（模糊）")
    private String recommendedDepartment;
    @Schema(description = "分诊状态 1待分诊 2AI分诊中 3AI完成 4待人工复审 5人工复审完成 6已生成方案")
    private Byte triageStatus;
    @Schema(description = "页码")
    private Integer pageNum = 1;
    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
