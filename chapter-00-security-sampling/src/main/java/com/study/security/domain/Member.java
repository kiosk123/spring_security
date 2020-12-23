package com.study.security.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.study.security.domain.base.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_MEMBERS")
public class Member extends BaseTimeEntity {
    
    @Id
    private String id;
    
    @Setter
    private String password;
    
    @Setter
    private String username;
    
    @Setter
    @ManyToOne
    @JoinColumn(name = "MEMBER_ROLE_ID")
    private MemberRole memberRole;

    public Member(String id, String password, String username, MemberRole memberRole) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.memberRole = memberRole;
    }
}
