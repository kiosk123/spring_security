package com.study.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.security.domain.MemberRole;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

    Optional<MemberRole> findByRoleName(String roleName);
}
