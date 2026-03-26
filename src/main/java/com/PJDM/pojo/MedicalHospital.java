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
 * <p>
 * 医院信息表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("medical_hospital")
public class MedicalHospital implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 医院编码，唯一索引
     */
    private String hospitalCode;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 医院英文名称
     */
    private String hospitalNameEn;

    /**
     * 医院类型（1：三甲医院；2：二甲医院；3：专科医院；4：国际医院）
     */
    private Byte hospitalType;

    /**
     * 医院等级（如三级甲等）
     */
    private String hospitalLevel;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区/县
     */
    private String district;

    /**
     * 医院地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 医院电话
     */
    private String phone;

    /**
     * 国际部电话
     */
    private String internationalPhone;

    /**
     * 医院网站
     */
    private String website;

    /**
     * 医院简介
     */
    private String introduction;

    /**
     * 特色科室（多个用逗号分隔）
     */
    private String featuredDepartments;

    /**
     * 预约须知
     */
    private String appointmentNotes;

    /**
     * 是否有国际部（1：是；2：否）
     */
    private Byte hasInternationalDept;

    /**
     * 是否合作（1：是；2：否）
     */
    private Byte isCooperation;

    /**
     * 合作开始日期
     */
    private LocalDate cooperationStartDate;

    /**
     * 医院Logo URL
     */
    private String logoUrl;

    /**
     * 封面图片URL
     */
    private String coverImageUrl;

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
