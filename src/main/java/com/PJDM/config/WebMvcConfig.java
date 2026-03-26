package com.PJDM.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.beans.PropertyEditorSupport;

/**
 * 全局 WebMvc 配置
 * 解决前端 select 空字符串传给后端 Byte/Integer/Long 类型时的转换异常
 * 原因：前端 <option value=""> 选中时传空串，Spring 无法将 "" 转为 Byte/Integer
 *
 * @author Alanine
 */
@RestControllerAdvice
public class WebMvcConfig {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 空字符串 → Byte 返回 null（忽略，不报错）
        binder.registerCustomEditor(Byte.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(Byte.parseByte(text.trim()));
                }
            }
        });

        // 空字符串 → Integer 返回 null
        binder.registerCustomEditor(Integer.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(Integer.parseInt(text.trim()));
                }
            }
        });

        // 空字符串 → Long 返回 null
        binder.registerCustomEditor(Long.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(Long.parseLong(text.trim()));
                }
            }
        });
    }
}
