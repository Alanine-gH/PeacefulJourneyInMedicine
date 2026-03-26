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
 * 专家信息表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("medical_expert")
public class MedicalExpert implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联用户ID（user_user.id），user_type=5（专家）
     */
    private Long userId;

    /**
     * 专家编码，唯一索引
     */
    private String expertCode;

    /**
     * 专家姓名
     */
    private String expertName;

    /**
     * 专家英文姓名
     */
    private String expertNameEn;

    /**
     * 所属医院ID，关联medical_hospital表
     */
    private Long hospitalId;

    /**
     * 医院名称（冗余）
     */
    private String hospitalName;

    /**
     * 所属科室ID，关联medical_department表
     */
    private Long departmentId;

    /**
     * 科室名称（冗余）
     */
    private String departmentName;

    /**
     * 职称（如主任医师、副主任医师）
     */
    private String professionalTitle;

    /**
     * 专业擅长
     */
    private String specialty;

    /**
     * 语言能力
     */
    private String languageAbility;

    /**
     * 性别
     */
    private Byte gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 照片URL
     */
    private String photoUrl;

    /**
     * 个人简介
     */
    private String introduction;

    /**
     * 工作年限
     */
    private Integer experienceYears;

    /**
     * 学历背景
     */
    private String educationBackground;

    /**
     * 发表论文数
     */
    private Integer publicationCount;

    /**
     * 预约费用
     */
    private BigDecimal appointmentFee;

    /**
     * 是否接受国际患者（1：是；2：否）
     */
    private Byte isAcceptInternational;

    /**
     * 是否推荐（1：是；2：否）
     */
    private Byte isRecommend;

    /**
     * 评分
     */
    private BigDecimal ratingScore;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 状态（1：正常；2：停用）
     */
    private Byte status;

    /**
     * 排序
     */
    private Integer sort;

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
