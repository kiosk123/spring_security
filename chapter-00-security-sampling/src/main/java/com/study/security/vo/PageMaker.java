package com.study.security.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import groovy.transform.ToString;
import lombok.Getter;

/**
 * 페이징 처리에 필요한 객체를 생성
 * @author USER
 */
@Getter
@ToString(excludes = "pageList")
public class PageMaker<T> {
    
    private Page<T> result;
    
    private Pageable prevPage; // 맨앞 페이지 '이전' 페이지로 이동
    private Pageable nextPage; // 맨뒤 페이지 '다음' 페이지로 이동
    
    private int currentPageNum;// 현재 페이지 번호
    private int totalPageNum;  // 전체 페이지 수
    
    private Pageable currentPage; // 현재 페이지 정보
    
    private List<Pageable> pageList; //페이지 번호의 시작부터 끝가지의 정보를 저장
    
    public PageMaker(Page<T> result) {
        this.result = result;
        this.currentPage = result.getPageable();
        this.currentPageNum = currentPage.getPageNumber() + 1;
        this.totalPageNum = result.getTotalPages();
        this.pageList = new ArrayList<>();
        
        calcPages();
    }

    private void calcPages() {
        int tempEndNum = (int)(Math.ceil(this.currentPageNum / 10.0) * 10);
        int startNum = tempEndNum - 9;
        Pageable startPage = this.currentPage;
        
        for (int i = startNum; i < this.currentPageNum; i++) {
            startPage = startPage.previousOrFirst();
        }
        this.prevPage = startPage.getPageNumber() <= 0? null : startPage.previousOrFirst();
        
        if (this.totalPageNum < tempEndNum) {
            tempEndNum = this.totalPageNum;
            this.nextPage = null;
        }
        
        for (int i = startNum; i <= tempEndNum; i++) {
            pageList.add(startPage);
            startPage = startPage.next();
        }
        
        this.nextPage = startPage.getPageNumber() + 1 < totalPageNum ? startPage: null;
    }
}
