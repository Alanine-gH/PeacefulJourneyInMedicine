package com.PJDM.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CheckinListVO {
    private Long id;
    private Long orderId;
    private Long accompanistId;
    /** 来自 user_accompanist 多表联查 */
    private String accompanistName;
    /** 来自 order_order 多表联查 */
    private String orderNo;
    private Byte checkinType;
    private String checkinLocation;
    private String checkinPhotos;
    private String checkinDescription;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime checkinTime;
    private LocalDateTime createTime;
}
