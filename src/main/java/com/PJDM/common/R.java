package com.PJDM.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 *
 * @author Alanine
 */
@Slf4j
@Data
public class R<T> {
    private Integer code; //编码：200成功，500和其它数字为失败
    private String msg; //错误信息
    private T data; //数据
    private Map map = new HashMap(); //动态数据

    @NotNull
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 200;
        r.msg = "success";
        return r;
    }

    @NotNull
    public static <T> R<T> warn(T object, String msg) {
        R<T> r = new R<T>();
        log.warn(msg);
        r.data = object;
        r.msg = msg;
        r.code = 200;
        return r;
    }
    @NotNull
    // <T> R<T> 静态方法返回值泛型定义格式
    public static <T> R<T> error(String msg) {
        R<T> r = new R<T>();
        log.error(msg);
        r.msg = msg;
        r.code = 500;
        return r;
    }
    @NotNull
    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
