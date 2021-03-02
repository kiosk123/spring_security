package com.study.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.security.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String>{

}
