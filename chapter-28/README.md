# 챕터 28 - 실전 프로젝트 폼(Form) 인증 구현 - CustomAuthenticationProvider DB연동 인증처리 - 2

이전 챕터에서 인증 정보를 담은 객체를 UserDetails 타입의 객체로 반환하였다
```java
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByUsername(username);
        if (account  == null) {
            throw new UsernameNotFoundException("username [" + username + "] is not found");
        }

        // 권한 정보 설정
        List<GrantedAuthority> roles = new ArrayList<>();

        //ROLE_USER, ROLE_ADMIN, ROLE_MANAGER 등등...
        roles.add(new SimpleGrantedAuthority(account.getRole()));

        AccountContext accountContext = new AccountContext(account, roles);
        return accountContext ;
    }

}
```
위와 같이 반환된 객체에 추가적인 검증을 진행하는 클래스인 AuthenticationProvider의 구현체를 직접 구현하여 인증처리가 되도록 처리한다.

```java
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
```

CustomAuthenticationProvider에서 UserDetailsService 서비스를 사용하고 있기 때문에 시큐리티 구성 클래스를 다음과 같이 수정하고  
인증처리를 CustomAuthenticationProvider가 처리하도록 등록한다.

```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()); // CustomAuthenticationProvider 등록
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    // PasswordEncoder 빈 생성
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/mypage").hasRole("USER")
            .antMatchers("/messages").hasRole("MANAGER")
            .antMatchers("/config").hasRole("ADMIN")
            .antMatchers("/", "/users", "/user/login/**").permitAll()
            .anyRequest().authenticated()
        .and()
            .formLogin();
    }
}
```


