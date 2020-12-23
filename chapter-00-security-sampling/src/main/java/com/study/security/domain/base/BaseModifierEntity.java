package com.study.security.domain.base;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseModifierEntity extends BaseTimeEntity {
    
    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;
    
    @LastModifiedBy
    protected String lastModifiedBy;
}
