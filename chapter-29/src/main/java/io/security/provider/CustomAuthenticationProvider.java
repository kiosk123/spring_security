package io.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.security.service.AccountContext;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 검증 구현
     */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName(); // 로그인 할 때 입력한 username
        String password = (String)authentication.getCredentials(); // 로그인 할 때 입력한 password

        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
            throw new BadCredentialsException("input password isn't correct");
        }

        UsernamePasswordAuthenticationToken authenticationToken 
            = new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());

		return authenticationToken;
	}

    /**
     * 파라미터로 전달되는 authentication의 타입과 
     * AuthenticationProvider 구현체가 사용하고자 하는 토큰의 타입과 일치할때 Provider가 인증처리하도록 
     * 참고하기 위한 메서드 
     */
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
    
}
