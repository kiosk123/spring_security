package com.study.security.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.study.security.domain.Board;
import com.study.security.domain.BoardReply;
import com.study.security.dto.BoardDTO;
import com.study.security.dto.BoardReplyDTO;
import com.study.security.repository.BoardReplyRepository;
import com.study.security.repository.BoardRepository;
import com.study.security.repository.querydsl.BoardQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    
    private final BoardRepository boardRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final BoardQueryRepository boardQueryRepository;
    
    public Page<BoardDTO> searchBoards(String type, String keyword, Pageable pageable) { 
        Page<Tuple> result = boardQueryRepository.searchBoards(type, keyword, pageable);
        return result.map(tuple -> { 
            return new BoardDTO(tuple.get(0, Long.class), 
                                tuple.get(1, String.class), 
                                tuple.get(2, String.class), 
                                tuple.get(3, String.class), 
                                tuple.get(4, Long.class),
                                tuple.get(5, LocalDateTime.class), 
                                tuple.get(6, LocalDateTime.class));
            });
    }
    
    @Transactional
    public void save(BoardDTO boardDTO) {
        Board board = new Board(boardDTO.getTitle(), boardDTO.getWriter(), boardDTO.getContent());
        boardRepository.save(board);
    }
    
    @Transactional
    public Long updateBoard(BoardDTO boardDTO) {
        Optional<Board> opt = boardRepository.findById(boardDTO.getBno());
        if (opt.isPresent()) {
            Board board = opt.get();
            board.setTitle(boardDTO.getTitle());
            board.setContent(boardDTO.getContent());
            return 1L;
        }
        else {
            return 0L;
        }
    }
    
    public Optional<BoardDTO> findByBno(Long bno) {
        Optional<Board> opt = boardRepository.findById(bno);
        if (opt.isPresent()) {
            Board board = opt.get();
            BoardDTO boardDTO = new BoardDTO(board.getBno(), 
                                             board.getTitle(), 
                                             board.getWriter(), 
                                             board.getContent(), 
                                             board.getCreatedDate(), 
                                             board.getUpdatedDate());
            return Optional.of(boardDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Transactional
    public Long deleteBoard(Long bno) {
        Optional<Board> opt = boardRepository.findById(bno);
        if (opt.isPresent()) {
            Board board = opt.get();
            boardRepository.delete(board);
            return 1L;
        }
        else {
            return 0L;
        }
    }
    
    @Transactional
    public void saveReply(Long bno, BoardReplyDTO boardReplyDTO) {
        Optional<Board> boardOpt = boardRepository.findById(bno);
        if (boardOpt.isPresent()) {
            Board board = boardOpt.get();
            BoardReply reply = new BoardReply(boardReplyDTO.getReplyText(), boardReplyDTO.getReplyer());
            board.addReply(reply);
        }
        else {
            //TODO 댓글처리하려는 게시물이 이미 삭제되어 있는 경우 처리
        }
    }
    
    @Transactional
    public Long deleteReply(Long rno) {
        Optional<BoardReply> opt = boardReplyRepository.findById(rno);
        if (opt.isPresent()) {
            BoardReply reply = opt.get();
            boardReplyRepository.delete(reply);
            return 1L;
        }
        else {
            return 0L;
        }
    }
    
    @Transactional
    public Long updateReply(BoardReplyDTO boardReplyDTO) {
        Optional<BoardReply> opt = boardReplyRepository.findById(boardReplyDTO.getRno());
        if (opt.isPresent()) {
            BoardReply reply = opt.get();
            reply.setReplyText(boardReplyDTO.getReplyText());
            return 1L;
        }
        else {
            return 0L;
        }
    }
    
    public List<BoardReplyDTO> getListByBoard(Long bno) {
        List<BoardReply> replies = boardReplyRepository.getRepliesOfBoard(bno);
        List<BoardReplyDTO> replyDTOs = new LinkedList<>();
        replies.forEach(reply -> {
            BoardReplyDTO replyDTO = new BoardReplyDTO(reply.getRno(),
                                                       reply.getReplyText(), 
                                                       reply.getReplyer(),
                                                       reply.getCreatedDate(),
                                                       reply.getUpdatedDate());
            replyDTOs.add(replyDTO);
        });
        return replyDTOs;
    }
}
