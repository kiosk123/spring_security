# 챕터 25 - 실전 프로젝트 폼(Form) 인증 구현 - WebIgnore 설정

js / css / image 파일 등 보안 필터를 적용할 필요가 없는 리소스를 설정한다.

```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // PasswordEncdoer 빈을 이용하여 평문을 암호화
        String password = passwordEncoder().encode("1111");

        auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
        auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER");
        auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN");
    }

    // Web Ignore 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        // PathRequest 패키지 경로 주의! - org.springframework.boot.autoconfigure.security.servlet.PathRequest
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
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
            .antMatchers("/").permitAll()
            .anyRequest().authenticated()
        .and()
            .formLogin();
    }
}

```