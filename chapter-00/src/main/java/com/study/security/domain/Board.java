package com.study.security.domain;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.study.security.domain.base.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_BOARDS")
public class Board extends BaseTimeEntity {
    
    @Id @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long bno;
    
    @Setter
    private String title;
    
    @Setter
    private String writer;
    
    @Setter
    private String content;
    
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoardReply> replies = new ArrayList<>();

    public Board(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }
    
    public void addReply(BoardReply reply) {
        reply.setBoard(this);
        replies.add(reply);
    }
}
