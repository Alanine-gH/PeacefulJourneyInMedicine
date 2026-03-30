package com.PJDM.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserIdentityAuthException extends RuntimeException {
    private int code;

    //认证状态为已认证的向前端返回300
    //但是这个好像没什么用！！
    public UserIdentityAuthException(String message) {
        super(message);
        log.warn(message);
        this.code = 300;
    }
}
