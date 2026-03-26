package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 就诊卡信息表
 *
 * @author Alanine
 * @since 2026-03-24
 */
@Getter
@Setter
@TableName("user_medical_card")
public class UserMedicalCard implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID，自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 就诊人ID，关联user_patient表 */
    private Long patientId;

    /** 所属用户ID，关联user_user表 */
    private Long userId;

    /** 关联医院ID */
    private Long hospitalId;

    /** 医院名称（冗余） */
    private String hospitalName;

    /** 就诊卡号 */
    private String cardNo;

    /** 状态（1：正常；2：停用） */
    private Byte status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
