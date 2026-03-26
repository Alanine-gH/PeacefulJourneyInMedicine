package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 调度派单表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("service_dispatch")
public class ServiceDispatch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 派单编号，唯一索引
     */
    private String dispatchNo;

    /**
     * 订单ID，关联order_order表
     */
    private Long orderId;

    /**
     * 订单号（冗余）
     */
    private String orderNo;

    /**
     * 陪诊师ID，关联user_accompanist表
     */
    private Long accompanistId;

    /**
     * 陪诊师姓名（冗余）
     */
    private String accompanistName;

    /**
     * 陪诊师手机号（冗余）
     */
    private String accompanistPhone;

    /**
     * 派单类型（1：自动派单；2：手动派单）
     */
    private Byte dispatchType;

    /**
     * 派单状态（1：待接单；2：已接单；3：已拒绝；4：已取消）
     */
    private Byte dispatchStatus;

    /**
     * 推送时间
     */
    private LocalDateTime pushTime;

    /**
     * 接单时间
     */
    private LocalDateTime receiveTime;

    /**
     * 拒绝时间
     */
    private LocalDateTime rejectTime;

    /**
     * 拒绝原因
     */
    private String rejectReason;

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
