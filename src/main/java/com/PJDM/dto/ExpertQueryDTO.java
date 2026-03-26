package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "专家查询参数")
public class ExpertQueryDTO {
    @Schema(description = "专家姓名（模糊）")
    private String expertName;
    @Schema(description = "医院ID")
    private Long hospitalId;
    @Schema(description = "科室ID")
    private Long departmentId;
    @Schema(description = "职称（模糊）")
    private String professionalTitle;
    @Schema(description = "是否推荐 1是 2否")
    private Byte isRecommend;
    @Schema(description = "状态 1正常 2停用")
    private Byte status;
    @Schema(description = "页码")
    private Integer pageNum = 1;
    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
