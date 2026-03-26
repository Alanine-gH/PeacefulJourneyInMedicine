package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("v_user_activity")
public class VUserActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    private Long id;

    /**
     * 用户名，唯一索引
     */
    private String username;

    /**
     * 用户类型（1：患者；2：陪诊师；3：客服；4：管理员）
     */
    private Byte userType;

    private Long loginCount;

    /**
     * 登录时间
     */
    private LocalDateTime lastLoginTime;

    private Long orderCount;

    private BigDecimal totalSpent;
}
