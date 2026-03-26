package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "服务套餐查询参数")
public class PackageQueryDTO {
    @Schema(description = "套餐名称（模糊）")
    private String packageName;
    @Schema(description = "套餐类型 1基础 2增值 3定制")
    private Byte packageType;
    @Schema(description = "服务类型 1全程陪诊 2代办手续 3接机 4送机 5翻译")
    private Byte serviceType;
    @Schema(description = "状态 1上架 2下架 3停售")
    private Byte status;
    @Schema(description = "页码")
    private Integer pageNum = 1;
    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
