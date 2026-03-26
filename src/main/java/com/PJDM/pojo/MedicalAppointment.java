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
 * 预约信息表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("medical_appointment")
public class MedicalAppointment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预约编号，唯一索引
     */
    private String appointmentNo;

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
     * 医院ID，关联medical_hospital表
     */
    private Long hospitalId;

    /**
     * 医院名称（冗余）
     */
    private String hospitalName;

    /**
     * 科室ID，关联medical_department表
     */
    private Long departmentId;

    /**
     * 科室名称（冗余）
     */
    private String departmentName;

    /**
     * 专家ID，关联medical_expert表
     */
    private Long expertId;

    /**
     * 专家姓名（冗余）
     */
    private String expertName;

    /**
     * 预约日期
     */
    private LocalDate appointmentDate;

    /**
     * 预约时间
     */
    private LocalTime appointmentTime;

    /**
     * 预约类型（1：初诊；2：复诊；3：会诊）
     */
    private Byte appointmentType;

    /**
     * 预约状态（1：待确认；2：已确认；3：已取消；4：已完成；5：已过期）
     */
    private Byte appointmentStatus;

    /**
     * 病情描述
     */
    private String diseaseDescription;

    /**
     * 就诊卡号
     */
    private String visitCardNo;

    /**
     * 挂号单号
     */
    private String registrationNo;

    /**
     * 签到时间
     */
    private LocalDateTime checkInTime;

    /**
     * 医嘱
     */
    private String doctorAdvice;

    /**
     * 处方URL
     */
    private String prescriptionUrls;

    /**
     * 是否需要复诊（1：是；2：否）
     */
    private Byte followUpNeeded;

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
