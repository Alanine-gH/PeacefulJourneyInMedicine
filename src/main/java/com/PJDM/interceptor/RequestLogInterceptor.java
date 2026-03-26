package com.PJDM.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/*
 * 请求日志拦截器
 * 记录每次 HTTP 请求的：URL、IP、耗时、响应状态码
 */
 /**
 * @author Alanine
 */
@Component
@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {

    /** ThreadLocal 存储请求开始时间 */
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        START_TIME.set(System.currentTimeMillis());

        String ip = getClientIp(request);
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String query = request.getQueryString();
        String fullUrl = query != null ? url + "?" + query : url;

        log.info("[请求入] {} {} | IP={} | UA={}",
                method, fullUrl, ip,
                abbreviate(request.getHeader("User-Agent"), 60));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        // 此处留空，afterCompletion 统一记录
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        long elapsed = System.currentTimeMillis() - START_TIME.get();
        START_TIME.remove(); // 防止内存泄漏

        int status = response.getStatus();
        String method = request.getMethod();
        String url = request.getRequestURL().toString();

        if (ex != null) {
            log.warn("[请求出] {} {} | status={} | elapsed={}ms | ex={}",
                    method, url, status, elapsed, ex.getMessage());
        } else if (elapsed > 2000) {
            // 慢请求警告（超过 2 秒）
            log.warn("[慢请求] {} {} | status={} | elapsed={}ms",
                    method, url, status, elapsed);
        } else {
            log.info("[请求出] {} {} | status={} | elapsed={}ms",
                    method, url, status, elapsed);
        }
    }

    /**
     * 获取真实客户端 IP（兼容代理）
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private String abbreviate(String s, int maxLen) {
        if (s == null) return "-";
        return s.length() <= maxLen ? s : s.substring(0, maxLen) + "...";
    }
}
