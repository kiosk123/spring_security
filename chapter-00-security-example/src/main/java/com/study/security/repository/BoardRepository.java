package com.study.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.security.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

}
