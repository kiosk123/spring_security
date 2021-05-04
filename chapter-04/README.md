# 챕터 04 - UsernamePasswordAuthenticationFilter의 이해

UsernamePasswordAuthenticationFilter는 사용자 로그인 인증처리를 하는 필터다  

<img src="./img/1.png" width="900" height="450">

### SecurityContext에 저장된 인증정보 사용하기
SecurityContext에 저장된 인증 객체는 다음과 같은 방식으로 전역적으로 호출해서 꺼내 사용할 수 있다.
```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
```