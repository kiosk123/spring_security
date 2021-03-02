package com.study.security.handler;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
       Object object = request.getSession().getAttribute("destUrl");
       
       String destUrl = null;
       
       if (!Objects.isNull(object) ) {
           request.getSession().removeAttribute("destUrl");
           
           destUrl = (String)object;
       }
       else {
           destUrl = super.determineTargetUrl(request, response);
       }
       
       return destUrl;
    }

}
