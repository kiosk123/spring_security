package com.study.security.domain.base;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseModifierEntity is a Querydsl query type for BaseModifierEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseModifierEntity extends EntityPathBase<BaseModifierEntity> {

    private static final long serialVersionUID = -2122492518L;

    public static final QBaseModifierEntity baseModifierEntity = new QBaseModifierEntity("baseModifierEntity");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath lastModifiedBy = createString("lastModifiedBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QBaseModifierEntity(String variable) {
        super(BaseModifierEntity.class, forVariable(variable));
    }

    public QBaseModifierEntity(Path<? extends BaseModifierEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseModifierEntity(PathMetadata metadata) {
        super(BaseModifierEntity.class, metadata);
    }

}

