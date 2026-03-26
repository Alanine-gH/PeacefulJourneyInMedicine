package com.PJDM.interceptor;

import com.PJDM.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ProjectHandler {
    @ExceptionHandler(SQLException.class)
    public R SqlExceptionHandler(Throwable throwable) {
        /*会员卡模板名字重复*/
        if (throwable.getMessage().contains("for key 'stad_card_temp.name'")) {
            //1.进行字符串撕裂，转换为一个数组
            String[] s = throwable.getMessage().split(" ");
            //2.解析数组s，数组s的内容为：
            //###, Error, updating, database., , Cause:,
            //java.sql.SQLIntegrityConstraintViolationException:,
            // Duplicate, entry, '至尊卡', for, key, 'stad_card_temp.name'
            System.out.println(Arrays.toString(s));
            //3.根据解析，获取重复的内容
            return R.error(s[9] + "已经存在");
        }
        /*其余情况*/
        return R.error("操作异常");
    }
}
