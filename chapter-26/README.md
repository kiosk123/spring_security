# 챕터 26 - 실전 프로젝트 폼(Form) 인증 구현 - 사용자 등록과 PasswordEncoder

## 1. PasswordEncoder  
비밀번호를 안전하게 암호화 하도록 제공  
SpringSecurity 5.0 이전에는 기본 PasswordEncoder가 평문을 지원하는 NoOpPasswordEncoder(현재는 Deprecated)  

### 1-1. 생성
```java
PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
```

DelegatingPasswordEncoder는 여러개의 PasswordEncoder 유형을 선언한 뒤, 상황에 맞게 선택해서 사용할 수 있도록 지원하는 Encoder이다.

```java
public static PasswordEncoder createDelegatingPasswordEncoder() {
    String encodingId = "bcrypt";
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put(encodingId, new BCryptPasswordEncoder());
    encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
    encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
    encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
    encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("scrypt", new SCryptPasswordEncoder());
    encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
    encoders.put("SHA-256",
            new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
    encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
    encoders.put("argon2", new Argon2PasswordEncoder());
    return new DelegatingPasswordEncoder(encodingId, encoders);
}
```

### 1-2. 암호화 포맷
bcrypt, noop, pbkdf2, scrypt, sha256 (기본 포맷은 Bcrypt : {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG )
### 1-3. PasswordEncoder 인터페이스 
```java
public interface PasswordEncoder {
    // 패스워드 암호화
	String encode(CharSequence rawPassword);

    // 패스워드 비교
	boolean matches(CharSequence rawPassword, String encodedPassword);

    // 암호화된 패스워드가 더 강력한 알고리즘으로 다시 암호화 해야되는지 여부 확인 기본은 false 반환
	default boolean upgradeEncoding(String encodedPassword) {
		return false;
	}

}
```

