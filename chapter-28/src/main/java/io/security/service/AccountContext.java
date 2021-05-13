package io.security.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import io.domain.entity.Account;
import lombok.Getter;

public class AccountContext extends User {

    @Getter
    private Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account; // 차후 참조를 위해 메모리에 저장
    }

}
