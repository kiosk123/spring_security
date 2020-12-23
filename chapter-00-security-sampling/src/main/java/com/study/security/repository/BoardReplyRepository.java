package com.study.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.study.security.domain.BoardReply;

public interface BoardReplyRepository  extends JpaRepository<BoardReply, Long>{
    
    @Query("select r from BoardReply r where r.board.bno = :bno and r.rno > 0 order by r.rno ASC")
    public List<BoardReply> getRepliesOfBoard(@Param("bno")Long bno);
    
//    @Query("select count(r) from BoardReply r where r.board.bno = :bno")
//    public Long getReplyCount(Long bno);
}
