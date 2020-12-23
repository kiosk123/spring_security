package com.study.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/guest/**").permitAll();
        http.authorizeRequests().antMatchers("/manager/**").hasRole("MANAGER");
        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
        
        // 인가 받은 권한이 없는 관계로 접근이 막혔다면 form 태그 기반 로그인을 통해 접근할 수 있다는 것을 알림 
        // http.formLogin(); //기본 로그인 페이지 사용
        
        http.formLogin().loginPage("/login");
        
        //권한이 없어 거부되었을 때 이동할 url 경로 지정
        http.exceptionHandling().accessDeniedPage("/accessDenied");
        http.logout().logoutUrl("/logout").invalidateHttpSession(true);
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
