package com.PJDM.untils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 日志捕获工具类
 * 用于捕获控制台输出并存储到内存中，以便后续写入文件
 *
 * @author Alanine
 * @Data 2025-12-12
 */
@Slf4j
@Component
public class LogCaptureUtil {

    private static final int MAX_LOG_LINES = 1000; // 最大保存的日志行数
    private final List<String> capturedLogs = new CopyOnWriteArrayList<>();
    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream logBuffer;

    private static boolean initialized = false;
    private static final Object initLock = new Object();

    @PostConstruct
    public void init() {
        // 确保只初始化一次
        synchronized (initLock) {
            if (initialized) {
                return;
            }
            initialized = true;
            
            // 保存原始的输出流
            originalOut = System.out;
            originalErr = System.err;

            // 创建自定义的输出流来捕获日志
            logBuffer = new ByteArrayOutputStream();
            PrintStream customOut = new CustomPrintStream(logBuffer, originalOut, originalErr);

            // 重定向系统输出流
            System.setOut(customOut);
            System.setErr(customOut);
        }
    }

    /**
     * 添加日志行到捕获列表
     */
    private void addLogLine(String line) {
        String timestampedLine = String.format("[%s] %s", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
            line);
        
        capturedLogs.add(timestampedLine);
        
        // 限制日志列表大小
        if (capturedLogs.size() > MAX_LOG_LINES) {
            capturedLogs.remove(0);
        }
    }

    /**
     * 获取捕获的日志内容
     */
    public List<String> getCapturedLogs() {
        return new ArrayList<>(capturedLogs);
    }

    /**
     * 清空已捕获的日志
     */
    public void clearCapturedLogs() {
        capturedLogs.clear();
    }

    /**
     * 获取并清空日志内容
     */
    public List<String> getAndClearLogs() {
        List<String> logs = new ArrayList<>(capturedLogs);
        clearCapturedLogs();
        return logs;
    }

    /**
     * 恢复原始输出流（谨慎使用）
     */
    public void restoreOriginalStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    /**
     * 自定义PrintStream类，用于捕获控制台输出
     */
    private class CustomPrintStream extends PrintStream {
        private final PrintStream originalOut;
        private final PrintStream originalErr;
        
        public CustomPrintStream(OutputStream out, PrintStream originalOut, PrintStream originalErr) {
            super(originalOut, true, Charset.defaultCharset());
            this.originalOut = originalOut;
            this.originalErr = originalErr;
        }
        
        @Override
        public void print(String s) {
            super.print(s);
            addLogLine(s);
        }
        
        @Override
        public void println(String s) {
            super.println(s);
            addLogLine(s);
        }
        
        @Override
        public void println(Object o) {
            super.println(o);
            addLogLine(String.valueOf(o));
        }
        
        @Override
        public void println() {
            super.println();
            addLogLine("");
        }
    }
}