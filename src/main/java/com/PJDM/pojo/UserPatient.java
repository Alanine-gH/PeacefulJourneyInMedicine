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
 * 就诊人信息表
 *
 * @author Alanine
 * @since 2026-03-24
 */
@Getter
@Setter
@TableName("user_patient")
public class UserPatient implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID，自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 所属用户ID，关联user_user表 */
    private Long userId;

    /** 姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 身份证号 */
    private String idCard;

    /** 性别（0：未知；1：男；2：女） */
    private Byte gender;

    /** 出生日期 */
    private LocalDate birthday;

    /** 与账户人关系（self:本人 spouse:配偶 parent:父母 child:子女 other:其他） */
    private String relation;

    /** 地址 */
    private String address;

    /** 是否默认就诊人（1：是；2：否） */
    private Byte isDefault;

    /** 状态（1：正常；2：停用） */
    private Byte status;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改人 */
    private String updateBy;

    /** 修改时间 */
    private LocalDateTime updateTime;

    /** 备注 */
    private String remark;
}
