package com.PJDM.common;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * 数学运算验证码文本生成器
 * 生成格式："1+2=@3" ，@ 之前是显示内容，@ 之后是答案
 *
 * @author Alanine
 */
public class MathKaptchaTextCreator extends DefaultTextCreator {

    private static final String[] OPERATORS = {"+", "-", "*"};

    @Override
    public String getText() {
        Random random = new Random();
        int a = random.nextInt(10) + 1;   // 1-10
        int b = random.nextInt(10) + 1;   // 1-10
        String op = OPERATORS[random.nextInt(OPERATORS.length)];

        int result;
        switch (op) {
            case "+":
                result = a + b;
                break;
            case "-":
                // 保证结果为正数
                if (a < b) { int tmp = a; a = b; b = tmp; }
                result = a - b;
                break;
            case "*":
                // 乘法范围限制小一点，避免数字太大
                a = random.nextInt(5) + 1;
                b = random.nextInt(5) + 1;
                result = a * b;
                break;
            default:
                result = a + b;
        }
        // 格式：显示文本@答案
        return a + " " + op + " " + b + " =@" + result;
    }
}
