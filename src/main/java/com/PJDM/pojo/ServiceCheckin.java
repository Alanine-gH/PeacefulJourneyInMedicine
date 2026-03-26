package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 打卡记录表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("service_checkin")
public class ServiceCheckin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID，关联order_order表
     */
    private Long orderId;

    /**
     * 陪诊师ID，关联user_accompanist表
     */
    private Long accompanistId;

    /**
     * 打卡类型（1：接机；2：送机；3：到达医院；4：进入诊室；5：检查结束；6：就诊结束；7：其他）
     */
    private Byte checkinType;

    /**
     * 打卡位置
     */
    private String checkinLocation;

    /**
     * 打卡照片（多个用逗号分隔，存储在MinIO）
     */
    private String checkinPhotos;

    /**
     * 打卡描述
     */
    private String checkinDescription;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 打卡时间
     */
    private LocalDateTime checkinTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 描述信息
     */
    private String remark;
}
