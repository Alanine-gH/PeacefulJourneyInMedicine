package com.PJDM.exception;

/**
 * 重复数据异常
 * 用于唯一索引冲突（编码已存在、单号重复等）场景
 *
 * @author Alanine
 */
public class DuplicateEntryException extends RuntimeException {

    private final String field;
    private final Object value;

    public DuplicateEntryException(String field, Object value) {
        super("[" + field + "] = " + value + " 已存在，不允许重复");
        this.field = field;
        this.value = value;
    }

    public DuplicateEntryException(String message) {
        super(message);
        this.field = null;
        this.value = null;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}
