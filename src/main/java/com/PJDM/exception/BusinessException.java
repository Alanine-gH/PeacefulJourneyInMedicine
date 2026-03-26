package com.PJDM.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用业务异常
 * 用于业务逻辑校验失败、状态流转不合法等场景
 *
 * @author Alanine
 */
@Slf4j
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        log.warn(message);
        this.code = 400;
    }

    public BusinessException(int code, String message) {
        super(message);
        log.warn(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
