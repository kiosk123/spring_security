package com.study.security.controller.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.security.dto.BoardReplyDTO;
import com.study.security.service.BoardService;

import lombok.extern.slf4j.Slf4j;





@RestController
@RequestMapping("/replies/*")
@Slf4j
public class BoardReplyController {
    
    @Autowired
    private BoardService boardService;
    
    @GetMapping("/{bno}")
    public ResponseEntity<List<BoardReplyDTO>> getReplies(@PathVariable("bno") Long bno) {
        List<BoardReplyDTO> replies = boardService.getListByBoard(bno);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }
    
    @Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @PostMapping("/{bno}")
    public ResponseEntity<List<BoardReplyDTO>> addReply(@PathVariable("bno")Long bno,
                                         @RequestBody BoardReplyDTO boardReplyDTO) {
        boardService.saveReply(bno, boardReplyDTO);
        List<BoardReplyDTO> replies = boardService.getListByBoard(bno);
        return new ResponseEntity<>(replies, HttpStatus.CREATED);
    }
    
    @Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @DeleteMapping("/{bno}/{rno}")
    public ResponseEntity<List<BoardReplyDTO>> deleteReply(@PathVariable("bno")Long bno,
                                                           @PathVariable("rno")Long rno) {

        boardService.deleteReply(rno);
        List<BoardReplyDTO> replies = boardService.getListByBoard(bno);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }
    
    @Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @PutMapping("/{bno}")
    public ResponseEntity<List<BoardReplyDTO>> modifyReply(@PathVariable("bno")Long bno,
                                                           @RequestBody BoardReplyDTO boardReplyDTO) {

        boardService.updateReply(boardReplyDTO);
        List<BoardReplyDTO> replies = boardService.getListByBoard(bno);
        return new ResponseEntity<>(replies, HttpStatus.CREATED);
    }
}
