package com.study.security.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
@NoArgsConstructor
public class BoardDTO {
    private Long bno;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long replyCount;
    
    private List<BoardReplyDTO> replies = new ArrayList<>();

    public BoardDTO(Long bno, String title, String writer, String content, LocalDateTime createdDate,
            LocalDateTime updatedDate) {
        this.bno = bno;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    
    public BoardDTO(Long bno, String title, String writer, String content, Long replyCount, LocalDateTime createdDate,
            LocalDateTime updatedDate) {
        this(bno, title, writer, content, createdDate, updatedDate);
        this.replyCount = replyCount;
    }
}
