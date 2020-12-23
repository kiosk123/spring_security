package com.study.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SampleController {
    
    @GetMapping("/")
    public String index() {
        log.info("index");
        return "securitytest/index";
    }
    
    @GetMapping("/guest")
    public String forGuest() {
        return "securitytest/guest";
    }
    
    @GetMapping("/manager")
    public String forManager() {
        return "securitytest/manager";
    }
    
    @GetMapping("/admin")
    public String forAdmin() {
        return "securitytest/admin";
    }
    
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/adminSecret")
    public String forAdminSecret() {
        log.debug("admin secret");
        return "securitytest/adminSecret";
    }
}
