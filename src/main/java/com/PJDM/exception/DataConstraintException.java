package com.PJDM.exception;

/**
 * 数据约束异常（唯一键、非空约束等）
 */
public class DataConstraintException extends RuntimeException {
    public DataConstraintException(String message) {
        super(message);
    }
}
