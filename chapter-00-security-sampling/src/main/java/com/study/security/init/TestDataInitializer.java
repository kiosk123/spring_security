package com.study.security.init;

import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.support.QueryMixin.Role;
import com.study.security.domain.Board;
import com.study.security.domain.BoardReply;
import com.study.security.domain.Member;
import com.study.security.domain.MemberRole;
import com.study.security.repository.BoardRepository;
import com.study.security.repository.MemberRepository;
import com.study.security.repository.MemberRoleRepository;

import lombok.RequiredArgsConstructor;

@Profile("dev")
@Component
public class TestDataInitializer {
    
    @Autowired
    private TestDataSetter testDataSetter;
    
    @PostConstruct
    void init() {
//        testDataSetter.insertBoardData();
        testDataSetter.insertRoleAndUserData();
    }

}

@Profile("dev")
@Component
@RequiredArgsConstructor
class TestDataSetter {
    
    private final BoardRepository boardRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final MemberRepository memberRepository;
    
    @Transactional
    void insertBoardData() {
        IntStream.range(0, 200).forEach(n -> {
            Board board = new Board("제목 : " + n, "user0" + (n % 10), "게시물 내용 ... " + n + " 내용 채우기");
            IntStream.range(0, 3).forEach(k -> {
                BoardReply boardReply = new BoardReply("댓글" + k, "user" + k);
                board.addReply(boardReply); 
            });
            boardRepository.save(board);
        });
    }
    
    @Transactional
    void insertRoleAndUserData() {
        MemberRole memberRole1 = new MemberRole("ADMIN");
        MemberRole memberRole2 = new MemberRole("MANAGER");
        MemberRole memberRole3 = new MemberRole("BASIC");
        
        memberRoleRepository.save(memberRole1);
        memberRoleRepository.save(memberRole2);
        memberRoleRepository.save(memberRole3);
        
        IntStream.range(0, 200).forEach(n -> {
            MemberRole role = null;
            if (n <= 80) {
                role = memberRole3;
            }
            else if (n <= 90) {
                role = memberRole2;
            } 
            else {
                role = memberRole1;
            }
            Member member = new Member("user" + n, "pw" + n, "사용자" + n, role);
            memberRepository.save(member);
        });
    }
    
}