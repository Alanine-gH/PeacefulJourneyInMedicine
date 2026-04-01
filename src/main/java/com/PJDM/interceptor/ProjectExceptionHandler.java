package com.PJDM.interceptor;

import com.PJDM.common.R;
import com.PJDM.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;

/*
 * 全局异常处理器
 * 优先级：具体异常 > 父类异常 > Exception
 */
/**
 * @author Alanine
 */
@RestControllerAdvice
@Slf4j
public class ProjectExceptionHandler {

    // ===== 业务异常：INFO 级别，属于预期内的业务拒绝 =====

    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusiness(BusinessException e) {
        log.info("[业务异常] code={} msg={}", e.getCode(), e.getMessage());
        return R.error(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public R<?> handleNotFound(ResourceNotFoundException e) {
        log.info("[资源不存在] resource={} id={} msg={}",
                e.getResourceName(), e.getResourceId(), e.getMessage());
        return R.error(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public R<?> handleValidation(ValidationException e) {
        log.info("[参数校验失败] field={} msg={}", e.getField(), e.getMessage());
        return R.error(e.getMessage());
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public R<?> handleDuplicate(DuplicateEntryException e) {
        log.info("[重复数据] field={} value={} msg={}",
                e.getField(), e.getValue(), e.getMessage());
        return R.error(e.getMessage());
    }

    @ExceptionHandler(FriendlyException.class)
    public R<?> handleFriendly(FriendlyException e) {
        log.info("[友好异常] msg={}", e.getMessage());
        return R.error(e.getMessage());
    }

    @ExceptionHandler(DataConstraintException.class)
    public R<?> handleConstraint(DataConstraintException e) {
        log.info("[数据约束异常] msg={}", e.getMessage());
        return R.error(e.getMessage());
    }

    // ===== Excel 导出异常：WARN 级别 =====

    @ExceptionHandler(ExcelExportException.class)
    public R<?> handleExcelExport(ExcelExportException e) {
        log.warn("[Excel导出异常] msg={}", e.getMessage(), e);
        return R.error("Excel 导出失败：" + e.getMessage());
    }

    // ===== 旧版自定义异常：兼容保留 =====

    @ExceptionHandler(MyException.class)
    public R<?> handleMyException(MyException e) {
        log.info("[MyException] msg={}", e.getMessage());
        return R.error(e.getMessage());
    }

    // ===== 数据库层异常 =====

    @ExceptionHandler(DataIntegrityViolationException.class)
    public R<?> handleDataIntegrity(DataIntegrityViolationException e) {
        String message = DbExceptionMessageTranslator.translate(e);
        log.warn("[数据完整性异常] msg={} raw={}", message,
                e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage());
        return R.error(message);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R<?> handleDuplicateKey(DuplicateKeyException e) {
        String message = DbExceptionMessageTranslator.translate(e);
        log.warn("[数据库唯一键冲突] msg={} raw={}", message, e.getMessage());
        return R.error(message);
    }

    @ExceptionHandler(SQLException.class)
    public R<?> handleSql(SQLException e) {
        String message = DbExceptionMessageTranslator.translate(e);
        if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
            log.warn("[SQL唯一键冲突] msg={} raw={}", message, e.getMessage());
            return R.error(message);
        }
        log.error("[SQL异常] msg={} raw={}", message, e.getMessage(), e);
        return R.error(message);
    }

    // ===== 参数类型异常 =====

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<?> handleMissingParam(MissingServletRequestParameterException e) {
        log.info("[缺少请求参数] param={} type={}", e.getParameterName(), e.getParameterType());
        return R.error("缺少必填参数：" + e.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.info("[参数类型错误] param={} value={}", e.getName(), e.getValue());
        return R.error("参数 [" + e.getName() + "] 类型错误");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public R<?> handleIllegalArg(IllegalArgumentException e) {
        log.info("[非法参数] msg={}", e.getMessage());
        return R.error("参数错误：" + e.getMessage());
    }

    // ===== MongoDB 异常：版本不兼容等，降级处理 =====

    @ExceptionHandler(UncategorizedMongoDbException.class)
    public R<?> handleMongo(UncategorizedMongoDbException e) {
        String msg = e.getMessage();
        if (msg != null && msg.contains("wire version")) {
            log.warn("[MongoDB版本不兼容] 本地 MongoDB 版本过低，请升级至 4.2+。msg={}", msg);
            return R.error("消息服务暂不可用，请联系管理员升级 MongoDB（需要 4.2+）");
        }
        log.error("[MongoDB异常] msg={}", msg, e);
        return R.error("消息服务暂时不可用，请稍后重试");
    }

    // ===== 兜底异常：ERROR 级别，打印堆栈 =====

    @ExceptionHandler(Exception.class)
    public R<?> handleGeneral(Exception e) {
        log.error("[未知异常] type={} msg={}", e.getClass().getSimpleName(), e.getMessage(), e);
        return R.error("系统繁忙，请稍后重试");
    }
}
