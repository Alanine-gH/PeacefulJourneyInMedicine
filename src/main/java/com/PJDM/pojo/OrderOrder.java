package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单主表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("order_order")
public class OrderOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号，唯一索引
     */
    private String orderNo;

    /**
     * 患者ID，关联user_user表
     */
    private Long userId;

    /**
     * 患者姓名（冗余）
     */
    private String userName;

    /**
     * 患者手机号（冗余）
     */
    private String userPhone;

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
     * 订单类型（1：全程陪诊；2：代办手续；3：接机服务；4：送机服务）
     */
    private Byte orderType;

    /**
     * 订单状态（1：待付款；2：已付款；3：已确认；4：服务中；5：已完成；6：已取消；7：退款中；8：已退款）
     */
    private Byte orderStatus;

    /**
     * 服务开始时间
     */
    private LocalDateTime serviceStartTime;

    /**
     * 服务结束时间
     */
    private LocalDateTime serviceEndTime;

    /**
     * 服务天数
     */
    private Integer serviceDays;

    /**
     * 服务小时数
     */
    private Integer serviceHours;

    /**
     * 患者姓名（用于就诊）
     */
    private String patientName;

    /**
     * 患者性别
     */
    private Byte patientGender;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 患者护照号
     */
    private String patientPassport;

    /**
     * 病情描述
     */
    private String diseaseDescription;

    /**
     * 预约医院（冗余）
     */
    private String appointmentHospital;

    /**
     * 预约科室（冗余）
     */
    private String appointmentDepartment;

    /**
     * 预约日期
     */
    private LocalDate appointmentDate;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 定金金额
     */
    private BigDecimal depositAmount;

    /**
     * 尾款金额
     */
    private BigDecimal balanceAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal actualPaidAmount;

    /**
     * 货币类型（USD、CNY、JPY等）
     */
    private String currency;

    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 航班到达时间
     */
    private LocalDateTime flightArrivalTime;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 酒店地址
     */
    private String hotelAddress;

    /**
     * 接机地址
     */
    private String pickUpAddress;

    /**
     * 送机地址
     */
    private String dropOffAddress;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 退款原因
     */
    private String refundReason;

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
