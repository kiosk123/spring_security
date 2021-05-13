package io.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String username;
    private String password;
    private String email;
    private String age;
    private String role;

	public Account(String username, String password, String email, String age, String role) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.age = age;
		this.role = role;
	}
}
