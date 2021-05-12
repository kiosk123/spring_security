package io.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountDto {
    String username;
    private String password;
    private String email;
    private String age;
    private String role;
}
