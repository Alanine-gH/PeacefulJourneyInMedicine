package com.PJDM.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 参数校验异常
 * 用于手机号格式、邮箱格式、必填字段为空等入参校验失败场景
 *
 * @author Alanine
 */
@Slf4j
public class ValidationException extends RuntimeException {

    private final String field;

    public ValidationException(String message) {
        super(message);
        this.field = null;
    }

    public ValidationException(String field, String message) {
        super("字段 [" + field + "] 校验失败：" + message);
        log.warn("字段 [" + field + "] 校验失败：" + message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
