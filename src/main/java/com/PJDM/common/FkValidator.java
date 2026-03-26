package com.PJDM.common;

import com.PJDM.exception.BusinessException;
import com.PJDM.exception.ResourceNotFoundException;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/*
 * 外键约束验证工具
 * 替代数据库外键，在 Service 层统一校验关联 ID 的存在性和合法性
 * 提供级联操作的业务层实现
 */
/**
 * @author Alanine
 */
@Slf4j
@Component
public class FkValidator {

    // ==================== 存在性校验 ====================

    /**
     * 校验关联 ID 存在性（通用）
     * 等价于数据库 FOREIGN KEY ... REFERENCES
     *
     * @param mapper       关联表的 Mapper
     * @param id           关联 ID
     * @param resourceName 资源名称（用于错误提示）
     * @throws ResourceNotFoundException 关联记录不存在时抛出
     */
    public <T> void requireExists(BaseMapper<T> mapper, Long id, String resourceName) {
        if (id == null) return; // null 值由 @NotNull 等注解校验，这里不处理
        if (mapper.selectById(id) == null) {
            log.warn("[外键校验] {} [{}] 不存在", resourceName, id);
            throw new ResourceNotFoundException(resourceName, id);
        }
    }

    /**
     * 校验关联 ID 存在性（ID 不能为 null）
     * 等价于 NOT NULL + FOREIGN KEY
     */
    public <T> void requireExistsNotNull(BaseMapper<T> mapper, Long id, String resourceName) {
        if (id == null) {
            throw new BusinessException(resourceName + "ID 不能为空");
        }
        requireExists(mapper, id, resourceName);
    }

    /**
     * 校验多个关联 ID（批量）
     */
    public <T> void requireAllExist(BaseMapper<T> mapper, Collection<Long> ids, String resourceName) {
        if (ids == null || ids.isEmpty()) return;
        for (Long id : ids) {
            requireExists(mapper, id, resourceName);
        }
    }

    // ==================== 级联删除保护 ====================

    /**
     * 禁止删除：存在子记录时拒绝删除父记录
     * 等价于数据库 ON DELETE RESTRICT
     *
     * @param childCount   子表关联记录数
     * @param parentName   父资源名称
     * @param childName    子资源名称
     */
    public void restrictDelete(long childCount, String parentName, String childName) {
        if (childCount > 0) {
            log.warn("[级联保护] 删除 {} 被拒绝，存在 {} 条关联 {} 记录", parentName, childCount, childName);
            throw new BusinessException(
                String.format("无法删除 %s，该记录下还有 %d 条关联的 %s，请先处理关联数据",
                    parentName, childCount, childName));
        }
    }

    /**
     * 状态校验：资源必须处于指定状态才能操作
     * 等价于数据库触发器的状态流转保护
     *
     * @param currentStatus  当前状态值
     * @param allowedStatus  允许的状态值
     * @param resourceName   资源名称
     * @param operationName  操作名称
     */
    public void requireStatus(Number currentStatus, Number allowedStatus,
                              String resourceName, String operationName) {
        if (currentStatus == null || !currentStatus.equals(allowedStatus)) {
            log.warn("[状态校验] {} 当前状态={}, 不允许执行 {}", resourceName, currentStatus, operationName);
            throw new BusinessException(
                String.format("%s 当前状态不允许执行 %s 操作（当前：%s，要求：%s）",
                    resourceName, operationName, currentStatus, allowedStatus));
        }
    }

    /**
     * 状态校验：资源必须处于多个允许状态之一
     */
    public void requireStatusIn(Number currentStatus, String resourceName,
                                String operationName, Number... allowedStatuses) {
        if (currentStatus == null) {
            throw new BusinessException(resourceName + " 状态异常");
        }
        for (Number allowed : allowedStatuses) {
            if (currentStatus.equals(allowed)) return;
        }
        log.warn("[状态校验] {} 当前状态={}, 不允许执行 {}", resourceName, currentStatus, operationName);
        throw new BusinessException(
            String.format("%s 当前状态（%s）不允许执行 %s 操作", resourceName, currentStatus, operationName));
    }
}
