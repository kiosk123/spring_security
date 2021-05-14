package io.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import io.domain.entity.Account;

public interface UserRepository extends JpaRepository<Account, Long> {
    
    Account findByUsername(String username);
    
}
