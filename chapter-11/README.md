# 챕터 11 - ExceptionTranslationFilter, RequestCacheAwareFilter의 이해

<img src="./img/1.png" width="900" height="450">
- AuthenticationEntryPoint를 직접 구현하여 처리할 수도 있음
- RequestCache : 사용자가 이전 요청정보(사용자가 이동하려고 시도한 URL등)을 저장하고 인증 성공시 RequestCache에 저장된 정보를 가져와서 사용
- AccessDeniedHandler를 직접 구현하여 처리할 수도 있음
<img src="./img/2.png" width="900" height="450">
<img src="./img/3.png" width="900" height="450">
<img src="./img/4.png" width="900" height="450">

### 예제 코드
```java
package study;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configurable
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 사용자 정보화 권한 정보 설정 - 여기서는 메모리 방식으로 처리
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 시큐리티 5부터 암호는 평문을 사용한다는 의미를 주기위해 {noop}를 사용
        auth.inMemoryAuthentication().withUser("user").password("{noop}1111").roles("USER");
        auth.inMemoryAuthentication().withUser("sys").password("{noop}1111").roles("SYS");
        auth.inMemoryAuthentication().withUser("admin").password("{noop}1111").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 권한 설정 및 표현식 처리 - 선언적 방식
         */
        http.authorizeRequests()
                .antMatchers("/login").permitAll() // login 페이지는 인증받지 않아도 이동할 수 있어야 하므로
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/sys").hasRole("SYS")
                .antMatchers("/admin/pay").hasRole("ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "SYS")
                .anyRequest().authenticated(); // 나머지 모든 요청에 인증 받아야함

        /**
         *  폼 로그인 처리
         */
        http.formLogin()
                // .loginPage("/loginPage") // login 페이지가 표시되는 URL 설정
                .defaultSuccessUrl("/")  // login 성공시 이동할 URL 설정
                // .failureUrl("/loginPage") // login 실패 후 이동할 URL 설정
                .usernameParameter("userId") // 설정안할시 디폴트는 username
                .passwordParameter("passwd") // 설정안할시 디폴트는 password
                .loginProcessingUrl("/login_proc") // form 태그의 action 애트리뷰트에 설정하는 URL

                //로그인 성공시 처리할 핸들러 설정
                .successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                            Authentication authentication) throws IOException, ServletException {
                        RequestCache requestCache = new HttpSessionRequestCache(); // 캐쉬에 담긴 사용자 요청했던 꺼내온다
                        SavedRequest savedRequest = requestCache.getRequest(request, response);
                        String redirectUrl = savedRequest.getRedirectUrl();
                        System.out.println("login success redirect : " + redirectUrl);
                        response.sendRedirect(redirectUrl); // 인증 후 사용자가 요청했던 정보로 이동
                    }
                })
                
                //로그인 실패시 처리할 핸들러
                .failureHandler(new AuthenticationFailureHandler() {

                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                            AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("exception : " + exception.getMessage());
                        response.sendRedirect("/login"); // 인증 실패 후 "/login"으로 이동
                    }
            })
            .permitAll() // 로그인 페이지( .loginPage() 통해서 설정 )는 누구나 접근가능해야 하므로(인증을 받지 않아도 접근해야 로그인이 가능하다)

        //... 생략

        /**
        * 인증 예외, 인가 예외처리
        */
        .and().exceptionHandling()
            .authenticationEntryPoint( (request, response, authException) -> response.sendRedirect("/login")) // 인증 예외 처리 -  여기서 login 처리는 프로그래머가 작성한 login 페이지를 뜻함(시큐리티가 제공한 페이지X)
            .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/denied")); // 인가 예외 처리
    }
}

```