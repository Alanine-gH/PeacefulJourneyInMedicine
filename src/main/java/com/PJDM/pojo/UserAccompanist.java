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
 * 陪诊师资质表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("user_accompanist")
public class UserAccompanist implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，关联user_user表
     */
    private Long userId;

    /**
     * 真实姓名（冗余user_user.real_name）
     */
    private String realName;

    /**
     * 手机号（冗余user_user.phone）
     */
    private String phone;

    /**
     * 性别（1：男；2：女；3：未知）
     */
    private Byte gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 职称（如主治医师、护士）
     */
    private String professionalTitle;

    /**
     * 医学背景（1：有医学背景；2：无医学背景）
     */
    private Byte medicalBackground;

    /**
     * 证书编号
     */
    private String certificateNumber;

    /**
     * 证书照片URL
     */
    private String certificatePhoto;

    /**
     * 无犯罪记录证明URL
     */
    private String criminalRecordCert;

    /**
     * 营业执照URL
     */
    private String businessLicense;

    /**
     * 语言能力（如英语、日语）
     */
    private String languageAbility;

    /**
     * 专业擅长（如骨科、脑科、妇产科）
     */
    private String specialties;

    /**
     * 已服务次数
     */
    private Integer serviceCount;

    /**
     * 评分（如4.85）
     */
    private BigDecimal ratingScore;

    /**
     * 准时率（如98.50）
     */
    private BigDecimal onTimeRate;

    /**
     * 投诉次数
     */
    private Integer complaintCount;

    /**
     * 陪诊状态（1：空闲；2：服务中；3：休假；4：停用）
     */
    private Byte accompanyStatus;

    /**
     * 审核状态（1：待审核；2：已通过；3：已拒绝）
     */
    private Byte auditStatus;

    /**
     * 审核通过时间
     */
    private LocalDateTime auditTime;

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
