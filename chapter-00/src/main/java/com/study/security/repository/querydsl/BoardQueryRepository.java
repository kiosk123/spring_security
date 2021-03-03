package com.study.security.repository.querydsl;

import static com.study.security.domain.QBoard.board;
import static com.study.security.domain.QBoardReply.boardReply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.study.security.domain.Board;
import com.study.security.repository.querydsl.base.Querydsl4Repository;

@Repository
public class BoardQueryRepository extends Querydsl4Repository {

    public BoardQueryRepository() {
        super(Board.class);
    }
    
    public Page<Tuple> searchBoards(String type, String keyword, Pageable pageable) {
        return applyPagination(pageable, query -> {
            return query.select(board.bno, 
                                board.title, 
                                board.writer, 
                                board.content,
                                JPAExpressions.select(boardReply.count())
                                .from(boardReply)
                                .where(boardReply.board.bno.eq(board.bno)),
                                board.createdDate, 
                                board.updatedDate)
                        .from(board)
                        .where(searchCondtion(type, keyword));
        }, countQuery -> {
            return countQuery.selectFrom(board).where(searchCondtion(type, keyword));
        });
    }
    
    private BooleanBuilder searchCondtion(String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        type = StringUtils.hasText(type) ? type.trim().toLowerCase() : "";
        switch (type) {
        case "t":
            builder.and(board.title.lower().like("%" + keyword.toLowerCase() + "%"));
            break;

        case "c":
            builder.and(board.content.lower().like("%" + keyword.toLowerCase() + "%"));
            break;
            
        case "w":
            builder.and(board.writer.lower().like("%" + keyword.toLowerCase() + "%"));
            break;
        }
        return builder;
    }
}
