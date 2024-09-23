package com.push.config.base;

import com.push.filter.TraceInterceptor;
import com.push.sso.JwtAuthorizationTokenInterceptor;
import com.push.sso.SessionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Description: Web统一配置
 * Create DateTime: 2020-03-26 13:47
 *
 *

 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${sso.session}")
    private Boolean session;

    @Resource
    private TraceInterceptor traceInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedMethods("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器，配置拦截地址
        if (session) {
            registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**")
                    .excludePathPatterns("/login/**", "/error", "/out/**", "/callback/**");
        } else {
            registry.addInterceptor(jwtAuthorizationTokenInterceptor()).addPathPatterns("/**")
                    .excludePathPatterns("/login/**", "/error", "/out/**", "/callback/**");
        }

        registry.addInterceptor(traceInterceptor).addPathPatterns("/**");
    }

    @Bean
    @ConditionalOnProperty(prefix = "sso", name = "session", havingValue = "true")
    public HandlerInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "sso", name = "session", havingValue = "false")
    public HandlerInterceptor jwtAuthorizationTokenInterceptor() {
        return new JwtAuthorizationTokenInterceptor();
    }
}
