package com.study.security.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.study.security.domain.PersistentLogins;

public interface RememberMeTokenRepository extends JpaRepository<PersistentLogins, String>{
    
    @Modifying
    @Query("update PersistentLogins p set p.token = :token, p.lastUsedTime = :lastUsedTime where p.series = :series")
    int updateTokenBySeries(@Param("token")String token, @Param("lastUsedTime")LocalDateTime lastUsedTime, @Param("series")String series);
    
    Optional<PersistentLogins> findBySeries(String series);
}
