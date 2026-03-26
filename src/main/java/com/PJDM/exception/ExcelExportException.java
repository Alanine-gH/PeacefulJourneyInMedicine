package com.PJDM.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Excel 导出异常
 * 用于 Excel 文件生成、写入流失败等场景
 *
 * @author Alanine
 */
@Slf4j
public class ExcelExportException extends RuntimeException {

    public ExcelExportException(String message) {
        super(message);
    }

    public ExcelExportException(String message, Throwable cause) {
        super(message, cause);
        log.warn(message, cause);
    }
}
