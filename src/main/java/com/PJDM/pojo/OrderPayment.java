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
 * 支付记录表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("order_payment")
public class OrderPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 支付流水号，唯一索引
     */
    private String paymentId;

    /**
     * 订单ID，关联order_order表
     */
    private Long orderId;

    /**
     * 订单号（冗余）
     */
    private String orderNo;

    /**
     * 支付用户ID，关联user_user表
     */
    private Long userId;

    /**
     * 用户姓名（冗余）
     */
    private String userName;

    /**
     * 支付类型（1：定金；2：尾款；3：全额；4：退款）
     */
    private Byte paymentType;

    /**
     * 支付方式（1：信用卡；2：支付宝；3：微信支付；4：Stripe；5：Adyen；6：银行转账）
     */
    private Byte paymentMethod;

    /**
     * 支付状态（1：待支付；2：支付中；3：支付成功；4：支付失败；5：已退款）
     */
    private Byte paymentStatus;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 人民币金额
     */
    private BigDecimal rmbAmount;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 支付渠道（如stripe、adyen、alipay、wechat）
     */
    private String paymentChannel;

    /**
     * 渠道交易号
     */
    private String channelTransactionId;

    /**
     * 渠道订单号
     */
    private String channelOrderId;

    /**
     * 付款人邮箱
     */
    private String payerEmail;

    /**
     * 付款人手机号
     */
    private String payerPhone;

    /**
     * 支付回调数据（JSON）
     */
    private String callbackData;

    /**
     * 回调时间
     */
    private LocalDateTime callbackTime;

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
