package com.PJDM.config;

import com.PJDM.common.JacksonObjectMapper;
import com.PJDM.interceptor.LoginInterceptor;
import com.PJDM.interceptor.RequestLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Spring MVC配置类
 *
 * @author Alanine
 */
@Slf4j
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLogInterceptor requestLogInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/back/**")
                .addResourceLocations("classpath:/back/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志拦截器（全局）
        registry.addInterceptor(requestLogInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/doc.html", "/webjars/**", "/swagger-ui/**", "/v3/api-docs/**");
        // JWT 登录拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/doc.html", "/webjars/**", "/swagger-ui/**",
                        "/v3/api-docs/**", "/swagger-resources", "/v2/api-docs",
                        "/common/download", "/user/login", "/user/register");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, converter);
    }
}
