package com.study.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MemberDTO {
    private String id;
    private String password;
    private String username;
    private String roleName;
}
