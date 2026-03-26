package com.PJDM.controller.monitor;

import com.PJDM.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.*;
import java.util.*;

/**
 * 服务器监控
 *
 * @author Alanine
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {

    @GetMapping
    public R<Map<String, Object>> getInfo() {
        Map<String, Object> result = new LinkedHashMap<>();

        // ===== JVM 信息 =====
        Runtime runtime = Runtime.getRuntime();
        long totalMem = runtime.totalMemory();
        long freeMem = runtime.freeMemory();
        long maxMem = runtime.maxMemory();
        long usedMem = totalMem - freeMem;

        Map<String, Object> jvm = new LinkedHashMap<>();
        jvm.put("javaVersion", System.getProperty("java.version"));
        jvm.put("jvmName", System.getProperty("java.vm.name"));
        jvm.put("totalMemoryMB", totalMem / 1024 / 1024);
        jvm.put("usedMemoryMB", usedMem / 1024 / 1024);
        jvm.put("freeMemoryMB", freeMem / 1024 / 1024);
        jvm.put("maxMemoryMB", maxMem / 1024 / 1024);
        jvm.put("memUsagePercent", Math.round(usedMem * 100.0 / maxMem));
        jvm.put("availableProcessors", runtime.availableProcessors());

        // JVM 启动时间
        RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
        jvm.put("startTime", new Date(runtimeMX.getStartTime()).toString());
        jvm.put("uptimeMinutes", runtimeMX.getUptime() / 1000 / 60);

        result.put("jvm", jvm);

        // ===== 系统信息 =====
        Map<String, Object> sys = new LinkedHashMap<>();
        sys.put("osName", System.getProperty("os.name"));
        sys.put("osArch", System.getProperty("os.arch"));
        sys.put("osVersion", System.getProperty("os.version"));
        sys.put("userDir", System.getProperty("user.dir"));
        sys.put("availableProcessors", runtime.availableProcessors());

        // CPU 使用率（通过 OperatingSystemMXBean）
        OperatingSystemMXBean osMX = ManagementFactory.getOperatingSystemMXBean();
        sys.put("systemLoadAverage", osMX.getSystemLoadAverage());
        if (osMX instanceof com.sun.management.OperatingSystemMXBean sunOS) {
            long cpuPct = Math.round(sunOS.getCpuLoad() * 100);
            sys.put("cpuUsagePercent", cpuPct < 0 ? 0 : cpuPct);
            sys.put("totalPhysicalMemoryMB", sunOS.getTotalMemorySize() / 1024 / 1024);
            sys.put("freePhysicalMemoryMB", sunOS.getFreeMemorySize() / 1024 / 1024);
            long memUsed = sunOS.getTotalMemorySize() - sunOS.getFreeMemorySize();
            sys.put("memUsagePercent", Math.round(memUsed * 100.0 / sunOS.getTotalMemorySize()));
        }
        result.put("sys", sys);

        // ===== 磁盘信息 =====
        List<Map<String, Object>> disks = new ArrayList<>();
        for (java.io.File root : java.io.File.listRoots()) {
            Map<String, Object> disk = new LinkedHashMap<>();
            long total = root.getTotalSpace();
            long free = root.getFreeSpace();
            long used = total - free;
            if (total == 0) continue;
            disk.put("path", root.getAbsolutePath());
            disk.put("totalGB", String.format("%.1f GB", total / 1024.0 / 1024 / 1024));
            disk.put("usedGB", String.format("%.1f GB", used / 1024.0 / 1024 / 1024));
            disk.put("freeGB", String.format("%.1f GB", free / 1024.0 / 1024 / 1024));
            disk.put("usagePercent", Math.round(used * 100.0 / total));
            disks.add(disk);
        }
        result.put("disks", disks);

        // ===== 线程信息 =====
        ThreadMXBean threadMX = ManagementFactory.getThreadMXBean();
        Map<String, Object> threads = new LinkedHashMap<>();
        threads.put("threadCount", threadMX.getThreadCount());
        threads.put("daemonThreadCount", threadMX.getDaemonThreadCount());
        threads.put("peakThreadCount", threadMX.getPeakThreadCount());
        result.put("threads", threads);

        return R.success(result);
    }
}
