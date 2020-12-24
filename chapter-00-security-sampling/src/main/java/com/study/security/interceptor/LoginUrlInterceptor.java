package com.study.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginUrlInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    
        String destUrl = request.getParameter("destUrl");
        if (StringUtils.hasText(destUrl)) {
            request.getSession().setAttribute("destUrl", destUrl);
        }
        return true;
    }
    
}
