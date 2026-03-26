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
 * 实名认证表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("user_identity_auth")
public class UserIdentityAuth implements Serializable {

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
     * 认证类型（1：护照；2：身份证；3：其他）
     */
    private Byte authType;

    /**
     * 实名认证的真实姓名
     */
    private String realName;

    /**
     * 身份证号（加密存储）
     */
    private String idCardNumber;

    /**
     * 护照号
     */
    private String passportNumber;

    /**
     * 护照照片正面URL
     */
    private String passportPhotoFront;

    /**
     * 护照照片背面URL
     */
    private String passportPhotoBack;

    /**
     * 身份证照片正面URL
     */
    private String idCardPhotoFront;

    /**
     * 身份证照片背面URL
     */
    private String idCardPhotoBack;

    /**
     * 认证状态（1：待审核；2：已通过；3：已拒绝）
     */
    private Byte authStatus;

    /**
     * 认证拒绝原因
     */
    private String authFailReason;

    /**
     * 认证通过时间
     */
    private LocalDateTime authTime;

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
