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
 * 通知公告表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("sys_notice")
public class SysNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 通知类型（1：系统公告；2：活动通知；3：版本更新）
     */
    private Byte noticeType;

    /**
     * 通知标题
     */
    private String noticeTitle;

    /**
     * 通知内容
     */
    private String noticeContent;

    /**
     * 通知链接
     */
    private String noticeLink;

    /**
     * 目标类型（1：全体用户；2：指定用户；3：指定角色）
     */
    private Byte targetType;

    /**
     * 目标ID列表
     */
    private String targetIds;

    /**
     * 发布状态（1：待发布；2：已发布；3：已撤回）
     */
    private Byte publishStatus;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

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
