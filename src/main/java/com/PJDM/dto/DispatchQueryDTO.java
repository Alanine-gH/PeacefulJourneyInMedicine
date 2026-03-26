package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "调度派单查询参数")
public class DispatchQueryDTO {
    @Schema(description = "派单编号")
    private String dispatchNo;
    @Schema(description = "订单号")
    private String orderNo;
    @Schema(description = "陪诊师姓名（模糊）")
    private String accompanistName;
    @Schema(description = "派单类型 1自动 2手动")
    private Byte dispatchType;
    @Schema(description = "派单状态 1待接单 2已接单 3已拒绝 4已取消")
    private Byte dispatchStatus;
    @Schema(description = "页码")
    private Integer pageNum = 1;
    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
