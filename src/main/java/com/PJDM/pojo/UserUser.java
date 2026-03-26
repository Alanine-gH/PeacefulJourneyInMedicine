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
 * 用户主表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("user_user")
public class UserUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，唯一索引
     */
    private String username;

    /**
     * 密码（加密存储）
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱地址，唯一索引
     */
    private String email;

    /**
     * 手机号，唯一索引
     */
    private String phone;

    /**
     * 国家代码（如+86、+1）
     */
    private String countryCode;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别（0：未知；1：男；2：女）
     */
    private Byte gender;

    /**
     * 用户类型（1：患者；2：陪诊师；3：客服；4：管理员）
     */
    private Byte userType;

    /**
     * 账号状态（1：活跃；2：停用；3：禁用）
     */
    private Byte status;

    /**
     * 语言设置（如zh-CN、en-US、ja-JP）
     */
    private String language;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

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
