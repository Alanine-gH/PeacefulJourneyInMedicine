package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "打卡记录查询参数")
public class CheckinQueryDTO {
    @Schema(description = "订单ID")
    private Long orderId;
    @Schema(description = "陪诊师ID")
    private Long accompanistId;
    @Schema(description = "打卡类型 1接机 2送机 3到达医院 4进入诊室 5检查结束 6就诊结束 7其他")
    private Byte checkinType;
    @Schema(description = "页码")
    private Integer pageNum = 1;
    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
