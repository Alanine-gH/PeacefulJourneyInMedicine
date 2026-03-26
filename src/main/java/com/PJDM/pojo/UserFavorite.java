package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 收藏表
 *
 * @author Alanine
 * @since 2026-03-24
 */
@Getter
@Setter
@TableName("user_favorite")
public class UserFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID，自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户ID，关联user_user表 */
    private Long userId;

    /** 收藏类型（companion：陪诊师；package：服务套餐；hospital：医院；expert：专家） */
    private String targetType;

    /** 收藏目标ID */
    private Long targetId;

    /** 创建时间 */
    private LocalDateTime createTime;
}
