package ru.otus.homework.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.homework.entity.QReferenceItemEntity;
import ru.otus.homework.entity.ReferenceEntity;
import ru.otus.homework.entity.ReferenceItemEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReferenceItemRepository extends PagingAndSortingRepository<ReferenceItemEntity, Long>, QuerydslPredicateExecutor<ReferenceItemEntity>,
        QuerydslBinderCustomizer<QReferenceItemEntity> {

    @Override
    default void customize(QuerydslBindings bindings, QReferenceItemEntity qEntity) {
        bindings.bind(qEntity.reference.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.code)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(qEntity.name)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(qEntity.brief)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }

    Optional<ReferenceItemEntity> findOne(Predicate predicate);

    List<ReferenceItemEntity> findAll(Predicate predicate);

    List<ReferenceItemEntity> findAll();

    @Query("from ReferenceItemEntity where id in :refItemIds")
    List<ReferenceItemEntity> findByRefItemIds(@Param(value = "refItemIds") Collection<Long> refItemIds);

    @Query("select i from ReferenceItemEntity i inner join ReferenceEntity r on r.id = i.reference.id and r.sysname = :referenceSysName")
    List<ReferenceItemEntity> findByReferenceSysNameEquals(@Param(value = "referenceSysName") String referenceSysName);

    @Query("select i from ReferenceItemEntity i inner join ReferenceEntity r on r.id = i.reference.id and r.sysname = :referenceSysName where i.code = :referenceItemCode")
    ReferenceItemEntity findByReferenceSysNameAndReferenceItemCodeEquals(@Param(value = "referenceSysName") String referenceSysName, @Param(value = "referenceItemCode") String referenceItemCode);

    void deleteAllByReferenceIdEquals(Long referenceId);

}
