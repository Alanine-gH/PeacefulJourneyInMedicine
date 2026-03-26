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
 * 就医建议书表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("medical_advice_book")
public class MedicalAdviceBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 建议书编号，唯一索引
     */
    private String adviceNo;

    /**
     * 分诊记录ID，关联medical_triage_record表
     */
    private Long triageId;

    /**
     * 患者ID，关联user_user表
     */
    private Long userId;

    /**
     * 建议书标题
     */
    private String adviceTitle;

    /**
     * 建议书内容
     */
    private String adviceContent;

    /**
     * 推荐选项（JSON格式，包含3-5个推荐医院/专家）
     */
    private String recommendedOptions;

    /**
     * 是否AI生成（1：是；2：否）
     */
    private Byte aiGenerated;

    /**
     * 是否人工复审（1：是；2：否）
     */
    private Byte humanReviewed;

    /**
     * 复审人ID
     */
    private Long reviewerId;

    /**
     * 复审时间
     */
    private LocalDateTime reviewTime;

    /**
     * 状态（1：待生成；2：待复审；3：已发布；4：已过期）
     */
    private Byte adviceStatus;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;

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
