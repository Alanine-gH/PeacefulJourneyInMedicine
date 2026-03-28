package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 服务套餐表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Data
@TableName("service_package")
public class ServicePackage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 套餐编码，唯一索引
     */
    private String packageCode;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 套餐英文名称
     */
    private String packageNameEn;

    /**
     * 套餐类型（1：基础套餐；2：增值套餐；3：定制套餐）
     */
    private Byte packageType;

    /**
     * 服务类型（1：全程陪诊；2：代办手续；3：接机服务；4：送机服务；5：翻译服务）
     */
    private Byte serviceType;

    /**
     * 套餐描述
     */
    private String description;

    /**
     * 服务内容（JSON格式）
     */
    private String serviceContent;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 单位（次、天、小时）
     */
    private String unit;

    /**
     * 服务时长（小时）
     */
    private Integer duration;

    /**
     * 最大使用次数（-1表示不限）
     */
    private Integer maxUsageCount;

    /**
     * 有效期（天）
     */
    private Integer validDays;

    /**
     * 封面图片URL
     */
    private String coverImageUrl;

    /**
     * 详情图片URL（多个用逗号分隔）
     */
    private String detailImageUrls;

    /**
     * 包含项目
     */
    private String includesItems;

    /**
     * 不包含项目
     */
    private String excludeItems;

    /**
     * 使用须知
     */
    private String usageNotice;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（1：上架；2：下架；3：停售）
     */
    private Byte status;

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
