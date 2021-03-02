package com.study.security.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardReplyDTO {
    private Long rno;
    private String replyText;
    private String replyer;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
