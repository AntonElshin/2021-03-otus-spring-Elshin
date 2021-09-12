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
import ru.otus.homework.entity.QReferenceEntity;
import ru.otus.homework.entity.ReferenceEntity;

import java.util.List;
import java.util.Optional;

public interface ReferenceRepository extends PagingAndSortingRepository<ReferenceEntity, Long>, QuerydslPredicateExecutor<ReferenceEntity>,
        QuerydslBinderCustomizer<QReferenceEntity> {

    @Override
    default void customize(QuerydslBindings bindings, QReferenceEntity qEntity) {
        bindings.bind(qEntity.group.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.name)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(qEntity.sysname)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }

    Optional<ReferenceEntity> findOne(Predicate predicate);

    List<ReferenceEntity> findAll(Predicate predicate);

    List<ReferenceEntity> findAll();

}
