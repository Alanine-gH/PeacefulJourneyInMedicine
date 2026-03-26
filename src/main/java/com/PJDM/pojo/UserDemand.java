package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 需求发布表
 *
 * @author Alanine
 * @since 2026-03-24
 */
@Getter
@Setter
@TableName("user_demand")
public class UserDemand implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID，自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 发布用户ID，关联user_user表 */
    private Long userId;

    /** 需求标题 */
    private String title;

    /** 需求描述 */
    private String description;

    /** 需求类型（accompany：陪护；nursing：护理；other：其他） */
    private String type;

    /** 医院ID */
    private Long hospitalId;

    /** 医院名称（冗余） */
    private String hospitalName;

    /** 科室ID */
    private Long departmentId;

    /** 科室名称（冗余） */
    private String departmentName;

    /** 就诊人ID */
    private Long patientId;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

    /** 预算金额 */
    private BigDecimal budget;

    /** 联系人姓名 */
    private String contactName;

    /** 联系人电话 */
    private String contactPhone;

    /** 特殊要求 */
    private String requirements;

    /** 城市 */
    private String city;

    /** 需求状态（1：待接单；2：已接单；3：已完成；4：已取消） */
    private Byte status;

    /** 接单陪诊师ID */
    private Long acceptAccompanistId;

    /** 接单时间 */
    private LocalDateTime acceptTime;

    /** 取消原因 */
    private String cancelReason;

    /** 刷新时间（用于排序） */
    private LocalDateTime refreshTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;

    /** 备注 */
    private String remark;
}
