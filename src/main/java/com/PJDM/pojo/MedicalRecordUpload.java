package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 病历资料表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("medical_record_upload")
public class MedicalRecordUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 病历编号，唯一索引
     */
    private String recordNo;

    /**
     * 患者ID，关联user_user表
     */
    private Long userId;

    /**
     * 分诊记录ID，关联medical_triage_record表
     */
    private Long triageId;

    /**
     * 病历类型（1：诊断证明；2：检查报告；3：处方单；4：手术记录；5：影像资料；6：其他）
     */
    private Byte recordType;

    /**
     * 病历标题
     */
    private String recordTitle;

    /**
     * 病历日期
     */
    private LocalDate recordDate;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 科室名称
     */
    private String departmentName;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 文件URL（多个文件用逗号分隔，存储在MinIO）
     */
    private String fileUrls;

    /**
     * 文件类型（如pdf、jpg、png）
     */
    private String fileTypes;

    /**
     * 原始文本内容
     */
    private String originalText;

    /**
     * 翻译后的文本内容
     */
    private String translatedText;

    /**
     * 翻译状态（1：待翻译；2：翻译中；3：翻译完成；4：翻译失败）
     */
    private Byte translateStatus;

    /**
     * 源语言（如en、ja、ko）
     */
    private String sourceLanguage;

    /**
     * 目标语言（如zh-CN）
     */
    private String targetLanguage;

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
