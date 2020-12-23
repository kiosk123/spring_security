package com.study.security.config;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class AuditingBeanConfiguration {
    
    @Profile("dev")
    @Bean("auditorProvider")
    public AuditorAware<String> auditorProvider1() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                //((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
                return Optional.of(UUID.randomUUID().toString());
            }
        };
    }
    
    @Profile("prod")
    @Bean("auditorProvider")
    public AuditorAware<String> auditorProvider2() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                HttpSession session 
                    = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                                                                      .getRequest()
                                                                      .getSession();
                //TODO
                return Optional.of("guest");
            }
        };
    }
    
}