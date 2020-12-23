# 스프링 부트 연습

### 프로젝트 구성
* 자바 11
* boot 2.4.0
* h2 데이터베이스 1.4.199
* querydsl
* spring data jpa
* lombok
* spring security5
* mybatis

## 타임리프 레이아웃 재사용 라이브러리
* [링크](https://ultraq.github.io/thymeleaf-layout-dialect/)

## HTML5 BolierPlate
* [링크](https://html5boilerplate.com)

## 챕터별 설명
 - 챕터 0 : 시큐리티 맛보기
    - 기본적으로 시큐리티 설정 후 부트 실행시 하나의 사용자 정보를 가지로록 세팅되어 있음
       - 이때 아이디는 'user'이며 비번은 다음과 같이 부트실행시 로그에 출력됨
         - Using generated security password: e6763f16-aa00-40aa-8adc-ad6df9457c90
    - 인메모리 인증, jdbc를 이용한 인증 구현은 SecurityConfig 클래스 참고
    - 모든 인증매니저는 결과적으로 UserDetails 타입의 객체를 반환
       - 인증 매니저를 커스터마이징하려면 UserDetailsService 인터페이스를 구현하여 사용
       - 구현은 UserService 클래스 참고
    - UserDetail 타입의 User 클래스를 상속받아서 인증관련 정보를 담을 객체를 커스터 마이징 가능하다.
       - 구현은 vo패키지의 SecurityUser 클래스를 참고
    - 스프링 시큐리티 정보를 Thymeleaf에서 사용하기 위해서는 Spring Security Dialect를 사용해야한다
       - [링크](https://github.com/thymeleaf/thymeleaf-extras-springsecurity)
       - 사용하고 있는 시큐리티 버전에 맞춰서 mvnrepository에서 다운 받는다
       - 이때 스프링 시큐리티 네임스페이스는 sec이다
          - 사용 예제는 admin.html 참고