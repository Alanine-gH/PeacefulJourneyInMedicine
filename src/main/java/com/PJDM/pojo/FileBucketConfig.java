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
 * 存储桶配置表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("file_bucket_config")
public class FileBucketConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 存储桶名称，唯一索引
     */
    private String bucketName;

    /**
     * 存储桶描述
     */
    private String bucketDescription;

    /**
     * 存储桶策略（JSON格式）
     */
    private String bucketPolicy;

    /**
     * 是否公开（1：是；2：否）
     */
    private Byte isPublic;

    /**
     * 最大文件大小（字节）
     */
    private Long maxFileSize;

    /**
     * 允许的文件扩展名
     */
    private String allowedExtensions;

    /**
     * 是否启用（1：是；2：否）
     */
    private Byte isEnable;

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
