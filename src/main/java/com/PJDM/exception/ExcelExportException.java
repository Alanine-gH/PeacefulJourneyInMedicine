package com.PJDM.exception;

/**
 * Excel 导出异常
 * 用于 Excel 文件生成、写入流失败等场景
 *
 * @author Alanine
 */
public class ExcelExportException extends RuntimeException {

    public ExcelExportException(String message) {
        super(message);
    }

    public ExcelExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
