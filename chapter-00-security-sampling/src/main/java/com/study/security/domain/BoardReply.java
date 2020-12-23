package com.study.security.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
@Table(name = "TBL_REPLIES")
public class BoardReply extends BaseTimeEntity {
    
    @Id @GeneratedValue
    @Column(name="REPLY_ID")
    private Long rno;
    
    @Setter
    private String replyText;
    
    @Setter
    private String replyer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    @Setter
    private Board board;
    
    public BoardReply(String replyText, String replyer) {
        this.replyText = replyText;
        this.replyer = replyer;;
    }
}
