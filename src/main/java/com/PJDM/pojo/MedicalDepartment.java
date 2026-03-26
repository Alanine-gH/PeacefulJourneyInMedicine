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
 * 科室信息表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("medical_department")
public class MedicalDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 科室编码，唯一索引
     */
    private String departmentCode;

    /**
     * 科室名称
     */
    private String departmentName;

    /**
     * 科室英文名称
     */
    private String departmentNameEn;

    /**
     * 父科室ID
     */
    private Long parentId;

    /**
     * 科室类型（1：内科；2：外科；3：妇产科；4：儿科；5：眼科；6：耳鼻喉科；7：皮肤科；8：骨科；9：神经科；10：其他）
     */
    private Byte departmentType;

    /**
     * 科室描述
     */
    private String description;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（1：正常；2：停用）
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
