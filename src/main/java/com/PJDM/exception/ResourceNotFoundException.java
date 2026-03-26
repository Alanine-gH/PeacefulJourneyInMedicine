package com.PJDM.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 资源不存在异常
 * 用于查询 getById 返回 null 的场景（对应 HTTP 404 语义）
 *
 * @author Alanine
 */
@Slf4j
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object resourceId;

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(resourceName + " [" + resourceId + "] 不存在");
        log.warn(resourceName + " [" + resourceId + "] 不存在");
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        log.warn(message);
        this.resourceName = "";
        this.resourceId = null;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Object getResourceId() {
        return resourceId;
    }
}
