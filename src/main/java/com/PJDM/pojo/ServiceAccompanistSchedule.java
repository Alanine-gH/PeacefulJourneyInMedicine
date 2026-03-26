package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 陪诊师日程表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("service_accompanist_schedule")
public class ServiceAccompanistSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 陪诊师ID，关联user_accompanist表
     */
    private Long accompanistId;

    /**
     * 日期
     */
    private LocalDate scheduleDate;

    /**
     * 班次（1：上午；2：下午；3：晚上；4：全天）
     */
    private Byte workShift;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 日程状态（1：可用；2：已预约；3：已占用；4：请假）
     */
    private Byte scheduleStatus;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 服务城市
     */
    private String serviceCity;

    /**
     * 服务地址
     */
    private String serviceAddress;

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
