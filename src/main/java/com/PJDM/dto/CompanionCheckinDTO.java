package com.PJDM.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 陪诊师打卡提交 DTO
 */
@Data
public class CompanionCheckinDTO {
    /** 订单ID */
    private Long orderId;
    /** 纬度 */
    private BigDecimal latitude;
    /** 经度 */
    private BigDecimal longitude;
    /** 打卡地址 */
    private String address;
    /** 打卡照片URL（多个逗号分隔） */
    private String photo;
    /** 打卡类型（1接机 2送机 3到达医院 4进入诊室 5检查结束 6就诊结束 7其他） */
    private Byte checkinType;
    /** 打卡描述 */
    private String description;
}
