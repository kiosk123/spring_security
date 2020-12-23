package com.study.security.vo;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.study.security.domain.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUser extends User {

    private static final String ROLE_PREFIX = "ROLE_";
    
    public SecurityUser(Member member) {
        // passwordEncoder 사용시 주석처리
//        super(member.getId(),  
//             "{noop}" + member.getPassword(), 
//             Arrays.asList(new SimpleGrantedAuthority(ROLE_PREFIX + member.getMemberRole().getRoleName())));
        
        super(member.getId(),  
              member.getPassword(), 
              Arrays.asList(new SimpleGrantedAuthority(ROLE_PREFIX + member.getMemberRole().getRoleName())));
    }
}
