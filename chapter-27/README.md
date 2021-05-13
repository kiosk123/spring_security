# 챕터 27 - 실전 프로젝트 폼(Form) 인증 구현 - CustomerUserDetailsService (DB 연동을 통한 인증처리 구현체 구현)

## 1. 메모리 인증 구성 삭제
DB를 이용해 인증처리를 하기 때문에 메모리 인증 구성을 삭제한다.
```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

/*  // 삭제 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // PasswordEncdoer 빈을 이용하여 평문을 암호화
        String password = passwordEncoder().encode("1111");

        auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
        auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER");
        auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN");
    } */
}
```

## 2. UserDetailsService를 구현하여 DB 연동을 통한 인증처리 구현체 구현

사용자 인증을 위한 객체는 UserDetails 인터페이스 구현하거나,  
UserDetails 인터페이스의 구현체인 `org.springframework.security.core.userdetails.User`를 상속받아 구현한다.

```java
public class AccountContext extends User {

    @Getter
    private Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account; // 차후 참조를 위해 메모리에 저장
    }

}
```

UserDetailsSerivce 인터페이스를 구현하여 DB 연동 인증 구현체를 작성한다.
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

구성 클래스에서는 `void configure(AuthenticationManagerBuilder auth)`를 WebSecurityConfigurerAdapter에서 오버라이드 한다.
```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    // void configure(AuthenticationManagerBuilder auth) 오버라이드 처리
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
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