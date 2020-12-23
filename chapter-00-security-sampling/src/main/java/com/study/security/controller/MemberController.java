package com.study.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.study.security.dto.MemberDTO;
import com.study.security.service.UserService;

@Controller
public class MemberController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/join")
    public void join() {

    }

    /*
     * @PostMapping("/join") public String joinPost(@ModelAttribute("member")Member
     * member){
     * 
     * log.info("MEMBER: " + member);
     * 
     * return "/member/joinResult"; }
     */

    @PostMapping("/join")
    public String joinPost(MemberDTO memberDTO, Model model) {
        userService.joinMember(memberDTO);
        model.addAttribute("member", memberDTO);
        return "/joinResult";
    }

}
