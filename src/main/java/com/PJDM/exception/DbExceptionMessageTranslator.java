package com.PJDM.exception;

/**
 * 数据库异常信息翻译器：把英文/长串异常转成前端可读中文
 */
public final class DbExceptionMessageTranslator {

    private DbExceptionMessageTranslator() {
    }

    public static String translate(Throwable e) {
        String msg = rootMessage(e);
        if (msg == null || msg.isBlank()) {
            return "数据操作失败，请稍后重试";
        }
        String lower = msg.toLowerCase();

        if (lower.contains("duplicate entry")) {
            if (lower.contains("user_user.email")) {
                if (msg.contains("Duplicate entry ''")) {
                    return "邮箱不能为空，且不能与其他用户重复";
                }
                return "邮箱已被使用，请更换后重试";
            }
            if (lower.contains("user_user.phone")) {
                return "手机号已被使用，请更换后重试";
            }
            if (lower.contains("user_user.username")) {
                return "用户名已存在，请更换后重试";
            }
            return "数据重复，请检查后重试";
        }

        if (lower.contains("cannot be null") || lower.contains("doesn't have a default value")) {
            return "存在必填字段为空，请完善后重试";
        }

        if (lower.contains("data too long")) {
            return "输入内容过长，请缩短后重试";
        }

        if (lower.contains("a foreign key constraint fails")) {
            return "关联数据不存在或已失效，请刷新页面后重试";
        }

        if (lower.contains("incorrect") && lower.contains("value")) {
            return "字段值格式不正确，请检查后重试";
        }

        return "数据操作异常，请稍后重试";
    }

    private static String rootMessage(Throwable e) {
        Throwable cur = e;
        String msg = null;
        while (cur != null) {
            if (cur.getMessage() != null && !cur.getMessage().isBlank()) {
                msg = cur.getMessage();
            }
            cur = cur.getCause();
        }
        return msg;
    }
}
