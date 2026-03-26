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
 * 订单评价表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("order_evaluation")
public class OrderEvaluation implements Serializable {

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
     * 订单号（冗余）
     */
    private String orderNo;

    /**
     * 评价用户ID，关联user_user表
     */
    private Long userId;

    /**
     * 被评价陪诊师ID，关联user_accompanist表
     */
    private Long accompanistId;

    /**
     * 评分（1-5分）
     */
    private BigDecimal ratingScore;

    /**
     * 评分维度（如服务态度、专业程度、准时程度）
     */
    private String ratingDimension;

    /**
     * 评价标签（如态度好、翻译准确、准时）
     */
    private String ratingLabels;

    /**
     * 评价内容
     */
    private String evaluationContent;

    /**
     * 评价图片（多张用逗号分隔）
     */
    private String evaluationImages;

    /**
     * 陪诊师回复
     */
    private String evaluationReply;

    /**
     * 回复时间
     */
    private LocalDateTime replyTime;

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
