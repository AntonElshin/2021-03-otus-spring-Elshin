package ru.otus.homework.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.homework.entity.QReferenceGroupEntity;
import ru.otus.homework.entity.ReferenceGroupEntity;

import java.util.List;
import java.util.Optional;

public interface ReferenceGroupRepository extends PagingAndSortingRepository<ReferenceGroupEntity, Long>, QuerydslPredicateExecutor<ReferenceGroupEntity>,
        QuerydslBinderCustomizer<QReferenceGroupEntity> {

    @Override
    default void customize(QuerydslBindings bindings, QReferenceGroupEntity qEntity) {
        bindings.bind(qEntity.parentId)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.name)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(qEntity.sysname)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }

    Optional<ReferenceGroupEntity> findOne(Predicate predicate);

    List<ReferenceGroupEntity> findAll(Predicate predicate);

    List<ReferenceGroupEntity> findAll();
}
