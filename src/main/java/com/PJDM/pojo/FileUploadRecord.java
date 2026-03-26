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
 * 文件上传记录表
 * </p>
 *
 * @author Alanine
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("file_upload_record")
public class FileUploadRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件ID，唯一索引
     */
    private String fileId;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 文件类型（如pdf、jpg、png、docx）
     */
    private String fileType;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * MIME类型
     */
    private String mimeType;

    /**
     * MinIO存储路径
     */
    private String storagePath;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * MinIO对象名称
     */
    private String objectName;

    /**
     * 文件访问URL
     */
    private String fileUrl;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 文件分类（1：病历资料；2：证件照片；3：证书资质；4：评价图片；5：打卡照片；6：系统文件）
     */
    private Byte fileCategory;

    /**
     * 关联业务类型（如triage、order、user、accompanist）
     */
    private String relatedType;

    /**
     * 关联业务ID
     */
    private Long relatedId;

    /**
     * 上传状态（1：上传中；2：上传成功；3：上传失败）
     */
    private Byte uploadStatus;

    /**
     * 病毒扫描状态（1：待扫描；2：扫描中；3：安全；4：危险）
     */
    private Byte virusScanStatus;

    /**
     * 上传人
     */
    private String uploadBy;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

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
