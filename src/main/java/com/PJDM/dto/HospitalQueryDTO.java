package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "医院查询参数")
public class HospitalQueryDTO {
    @Schema(description = "医院名称（模糊）")
    private String hospitalName;
    @Schema(description = "城市")
    private String city;
    @Schema(description = "医院类型 1三甲 2二甲 3专科 4国际")
    private Byte hospitalType;
    @Schema(description = "是否合作 1是 2否")
    private Byte isCooperation;
    @Schema(description = "状态 1正常 2停用")
    private Byte status;
    @Schema(description = "页码")
    private Integer pageNum = 1;
    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
