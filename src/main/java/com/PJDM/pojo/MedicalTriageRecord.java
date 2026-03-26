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
 * 智能分诊记录表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("medical_triage_record")
public class MedicalTriageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分诊单号，唯一索引
     */
    private String triageNo;

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
     * 性别
     */
    private Byte gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 主诉
     */
    private String chiefComplaint;

    /**
     * 症状描述
     */
    private String symptomDescription;

    /**
     * 症状图片URL（多张用逗号分隔）
     */
    private String symptomImageUrls;

    /**
     * 疼痛部位（如头部、胸部、腹部）
     */
    private String painLocation;

    /**
     * 疼痛等级（1-10）
     */
    private Byte painLevel;

    /**
     * 疼痛持续时间
     */
    private String painDuration;

    /**
     * 既往病史
     */
    private String medicalHistory;

    /**
     * 过敏史
     */
    private String allergyHistory;

    /**
     * 当前用药
     */
    private String currentMedication;

    /**
     * 分诊结果（AI分析结果）
     */
    private String triageResult;

    /**
     * 推荐科室
     */
    private String recommendedDepartment;

    /**
     * 推荐医院（多个用逗号分隔）
     */
    private String recommendedHospitals;

    /**
     * 分诊状态（1：待分诊；2：AI分诊中；3：AI分诊完成；4：待人工复审；5：人工复审完成；6：已生成方案）
     */
    private Byte triageStatus;

    /**
     * AI分诊置信度
     */
    private BigDecimal aiConfidence;

    /**
     * AI分析时间
     */
    private LocalDateTime aiAnalysisTime;

    /**
     * 人工复审时间
     */
    private LocalDateTime humanReviewTime;

    /**
     * 人工复审人ID
     */
    private Long humanReviewerId;

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
