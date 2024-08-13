package com.four.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    // 攔截器的部分
    @Bean
    AuthenticationInterceptor authenticationInterceptor() {
    	return new AuthenticationInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/public/**", "/public/login");
    }
}
