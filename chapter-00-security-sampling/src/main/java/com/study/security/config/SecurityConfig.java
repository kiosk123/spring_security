package com.study.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.study.security.service.RememberMeTokenService;
import com.study.security.service.UserService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RememberMeTokenService rememberMeTokenService;
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/guest/**").permitAll();
        http.authorizeRequests().antMatchers("/manager/**").hasRole("MANAGER");
        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
        
        //게시물 권한 관리 - 여기서 설정 또는 @Secured 애너테이션을 이용해서 직접 설정 가능
        http.authorizeRequests()
            .antMatchers("/board/list").permitAll()
            .antMatchers("/board/register")
            .hasAnyRole("BASIC", "MANAGER", "ADMIN");
        
        // 인가 받은 권한이 없는 관계로 접근이 막혔다면 form 태그 기반 로그인을 통해 접근할 수 있다는 것을 알림 
        // http.formLogin(); //기본 로그인 페이지 사용
        
        http.formLogin().loginPage("/login");
        
        //권한이 없어 거부되었을 때 이동할 url 경로 지정
        http.exceptionHandling().accessDeniedPage("/accessDenied");
        http.logout().logoutUrl("/logout").invalidateHttpSession(true);
        http.userDetailsService(userService); //사용할 인증 서비스 등록
        
        // remember-me 처리 - remember-me이름의 쿠키를 통해 자동로그인 처리(사용자가 비번을 바꾸면 로그인 되지 않는 현상 발생할 수 있음)
        // http.rememberMe().key("remember-me").userDetailsService(userService); //패스워드 인코더 사용시 주석처리
        
        //데이터베이스를 이용하여 처리하는 방식권장
        //1.데이터베이스를 이용하여 처리하는 방식 JdbcTokenRepositoryImpld을 이용 - 그전에 security.sql에 정의한 테이블부터 생성후 사용할 것
        //쿠키생성은 패스워드가 아닌 series에 있는 값을 기준으로함
//        http.rememberMe()
//            .key("remember-me")
//            .userDetailsService(userService)
//            .tokenRepository(getJDBCRepository())
//            .tokenValiditySeconds(60 * 60 * 24); // 24시간 유지
        
        //2.데이터베이스를 이용하여 처리하는 방식 PersistentTokenRepository 구현체을 이용 
        //쿠키생성은 패스워드가 아닌 series에 있는 값을 기준으로함
        http.rememberMe()
            .key("remember-me")
            .userDetailsService(userService)
            .tokenRepository(rememberMeTokenService)
            .tokenValiditySeconds(60 * 60 * 24); // 24시간 유지
            
            
    }
    
    private PersistentTokenRepository getJDBCRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }
    
      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
          //인증에 사용할 패스워드 인코더 등록
          auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
      }

    /*
     * 인메모리 방식의 인증 처리
     */
    // 시큐리티 인증 규칙 설정 {noop}은 암호와 없이 패스워드를 비교한다는 뜻
    // 없으면 There is no PasswordEncoder mapped for the id "null" 에러 발생
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//        .withUser("manager")
//        .password("{noop}1111")
//        .roles("MANAGER");
//    }
    
  /*
   * JDBC를 이용한 인증 처리
   */
//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//      //조회시 username, password, enabled 칼럼 데이터가 필요 컬럼명과 다를경우 SQL에서 컬럼명을 이용해 조회
//      //enabled 칼럼은 해당 계정이 사용가능한지 여부를 의미한다 - 적절한 데이터가 없다면 true를 이용하도록 설정한다.
//      //{noop}은 There is no PasswordEncoder mapped for the id "null" 에러 발생을 막기위해 사용
//      String query1 = "SELECT id username, concat('{noop}', password) password, true enabled From tbl_members WHERE id=?";
//      
//      String query2 = "select u.id username, a.role_name authority from tbl_members u,tbl_member_roles a "
//                    + "where u.member_role_id = a.id and u.id=?";
//      
//      auth.jdbcAuthentication()
//          .dataSource(dataSource)
//          .usersByUsernameQuery(query1)
//          .rolePrefix("ROLE_") //권한 문자열 앞에 ROLE_ 문자열을 붙여준다
//          .authoritiesByUsernameQuery(query2);
//  }
}
