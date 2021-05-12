# 챕터 24 - 실전 프로젝트 폼(Form) 인증 구현 - 프로젝트 구성 및 시큐리티 설정 클래스 구현

## 프로젝트 구성 - postgressql과 maven을 활용한 구성은 [링크](https://github.com/onjsdnjs/corespringsecurity) 참고
- gradle - 프로젝트 라이프 사이클 관리
- h2 데이터베이스
- lombok
- jpa
- querydsl
- spring mvc
- thymeleaf
- security

## build.gradle 내용

```gradle
plugins {
	id 'org.springframework.boot' version '2.4.0'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	id 'java'
}

group = 'study'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// query 로그 보는 라이브러리
	implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6' 
	
	// querydsl 추가
	implementation 'com.querydsl:querydsl-jpa'
	
	// 타임리프 레이아웃 라이브러리
	implementation 'nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.5.1'

	// 타임리프
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'

	// jpa
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

	developmentOnly 'org.springframework.boot:spring-boot-devtools'

	// h2 데이터 베이스 
	runtimeOnly 'com.h2database:h2'

	// web mvc
	implementation 'org.springframework.boot:spring-boot-starter-web'

	// 시큐리티
	implementation 'org.springframework.boot:spring-boot-starter-security'

	// 객체 프로퍼티간 맵핑시 유용 (VO -> DTO )http://modelmapper.org/getting-started/
	implementation 'org.modelmapper:modelmapper:2.4.3'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
	useJUnitPlatform()
}
```
## application.yml 설정 - [참고](https://engkimbs.tistory.com/794)
```yml
spring:
  profiles:
    active: dev
  data:
    web:
      pageable:
        max-page-size: 50
        default-page-size: 20
server:
  servlet:
    encoding:
      force: true
      enabled: true
      charset: UTF-8
logging:
  level:
    study: debug 
    org.hibernate.SQL: debug #logger를 통해 출력
    org.hibernate.type: trace #SQL 쿼리 파라미터를 확인할 수 있다
    org.spring.framework.web: debug 
    org.springframework.security: debug
    
---
spring:
  profiles:
  - dev
  datasource:
    url: jdbc:h2:tcp://localhost/~/security
    
    username: sa
    password: 
    driver-class-name: org.h2.Driver

  devtools:
    livereload:
      enabled: true
    restart:
      enabled: true
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        # 드라이버가 createClub을 지원하지 않아서 warning 뜨는 것을 방지
        jdbc.lob.non_contextual_creation: true 
#        show_sql: true # System.out을 통해 출력
        format_sql: true
        use_sql_comments: true
        dialect: org.hibernate.dialect.H2Dialect
        default_batch_fetch_size: 100
---
spring:
  profiles:
  - prod
  datasource:
    url: jdbc:h2:tcp://localhost/~/security
    
    username: sa
    password: 
    driver-class-name: org.h2.Driver
    
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
#        show_sql: true # System.out을 통해 출력
        format_sql: true
        use_sql_comments: true
        dialect: org.hibernate.dialect.H2Dialect
        default_batch_fetch_size: 100
```

## 시큐리티 설정 클래스 구현

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
