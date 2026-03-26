package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.config.MinioConfig;
import com.PJDM.exception.MyException;
import com.PJDM.untils.MinioUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * 文件上传和下载控制器
 *
 * @author Alanine
 */
@CrossOrigin
@RestController
@Slf4j
public class CommonController {
    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private MinioConfig minioConfig;

    /**
     * 接收前端传输的图片
     * 接口路径: /upload
     */
    @PostMapping("/upload")
    public R<String> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("开始上传文件: {}", file.getOriginalFilename());
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                log.error("上传文件为空");
                return R.error("上传文件不能为空");
            }

            // 检查文件大小（限制为10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                log.error("文件大小超过限制: {} bytes", file.getSize());
                return R.error("文件大小不能超过10MB");
            }

            // 获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return R.error("文件名不能为空");
            }

            // 获取文件扩展名
            String extension = StringUtils.substringAfterLast(originalFilename, ".");
            if (StringUtils.isEmpty(extension)) {
                return R.error("文件格式不正确");
            }

            // 生成新的文件名（UUID + 扩展名）
            String newFileName = UUID.randomUUID().toString() + "." + extension;
            log.info("生成新文件名: {}", newFileName);

            // 获取文件类型
            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 上传到MinIO
            minioUtils.uploadFile(minioConfig.getBucketName(), file, newFileName, contentType);

            // 返回文件名（前端需要拼接完整的访问URL）
            log.info("文件上传成功: {}", newFileName);
            return R.success(newFileName);

        } catch (Exception e) {
            log.error("文件上传失败", e);
            return R.error("文件上传失败: " + e.getMessage());
        }
    }

    //接收文件对象
    //1.必须用MultipartFile 来接收
    //2.文件名必须与file参数名一致
    @PostMapping("/common/upload")
    public R<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            //1.获取传递的文件名称
            String filename = file.getOriginalFilename();
            //1.1 将传递的文件名进行截取 然后加工
            String str = StringUtils.substringAfterLast(filename, ".");
            String rawFileName = UUID.randomUUID().toString() + "." + str;
            System.err.println(rawFileName);
            //2.将指定文件传入到minio服务器上
            minioUtils.uploadFile(minioConfig.getBucketName(), file, rawFileName, file.getContentType());
            return R.success(rawFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败");
        }
    }

    /**
     * 文件下载（图片预览）
     *
     * @param fileName 文件名（MinIO objectName）
     * @param response HttpServletResponse
     */
    @GetMapping("/common/download")
    public void download(@RequestParam("name") String fileName, HttpServletResponse response) {
        try {
            // 根据扩展名推断 Content-Type，让浏览器/小程序能正常渲染图片
            String lower = fileName.toLowerCase();
            String contentType;
            if (lower.endsWith(".png")) {
                contentType = "image/png";
            } else if (lower.endsWith(".gif")) {
                contentType = "image/gif";
            } else if (lower.endsWith(".webp")) {
                contentType = "image/webp";
            } else if (lower.endsWith(".bmp")) {
                contentType = "image/bmp";
            } else {
                // 默认 jpeg，兼容 .jpg .jpeg 及未知扩展名
                contentType = "image/jpeg";
            }
            response.setContentType(contentType);
            // inline 让小程序/浏览器直接渲染而非下载
            response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
            // 允许跨域（小程序 webview 场景）
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream fileInputStream = minioUtils.getObject(
                    minioConfig.getBucketName(), fileName);
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("文件下载失败: {}", fileName, e);
            throw new MyException(e.getMessage() + "下载失败");
        }
    }
}
