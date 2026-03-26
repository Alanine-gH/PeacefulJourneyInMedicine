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
 * 增值服务订单表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("value_added_order")
public class ValueAddedOrder implements Serializable {

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
     * 用户ID，关联user_user表
     */
    private Long userId;

    /**
     * 用户姓名（冗余）
     */
    private String userName;

    /**
     * 用户手机号（冗余）
     */
    private String userPhone;

    /**
     * 服务类型（1：病历翻译；2：处方解读；3：远程随访；4：视频复诊；5：术后康养）
     */
    private Byte serviceType;

    /**
     * 订单状态（1：待付款；2：已付款；3：服务中；4：已完成；5：已取消）
     */
    private Byte orderStatus;

    /**
     * 原订单ID，关联order_order表
     */
    private Long originalOrderId;

    /**
     * 服务费用
     */
    private BigDecimal serviceFee;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 服务内容描述
     */
    private String serviceContent;

    /**
     * 交付时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 完成时间
     */
    private LocalDateTime completionTime;

    /**
     * 交付文件URL
     */
    private String fileUrls;

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
