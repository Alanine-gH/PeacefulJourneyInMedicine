package com.PJDM.exception;

/**
 * 可直接展示给前端的友好异常
 */
public class FriendlyException extends RuntimeException {
    public FriendlyException(String message) {
        super(message);
    }
}
