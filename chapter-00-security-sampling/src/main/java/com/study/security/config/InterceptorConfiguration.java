package com.study.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.study.security.interceptor.LoginUrlInterceptor;


@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUrlInterceptor()).addPathPatterns("/login");
    }

    @Bean
    public LoginUrlInterceptor loginUrlInterceptor() {
        return new LoginUrlInterceptor();
    }
    
}
