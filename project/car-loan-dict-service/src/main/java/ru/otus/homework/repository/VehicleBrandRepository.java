package ru.otus.homework.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.homework.entity.QVehicleBrandEntity;
import ru.otus.homework.entity.VehicleBrandEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VehicleBrandRepository extends PagingAndSortingRepository<VehicleBrandEntity, Long>, QuerydslPredicateExecutor<VehicleBrandEntity>,
        QuerydslBinderCustomizer<QVehicleBrandEntity> {

    @Override
    default void customize(QuerydslBindings bindings, QVehicleBrandEntity qEntity) {
        bindings.bind(qEntity.name)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(qEntity.productionKind.id)
                .first(SimpleExpression::eq);
    }

    Optional<VehicleBrandEntity> findByNameEqualsIgnoreCase(String name);

    List<VehicleBrandEntity> findAll();

    @Query("from VehicleBrandEntity where id in :ids")
    List<VehicleBrandEntity> findAllByIds(@Param(value = "ids") Collection<Long> ids);

    List<VehicleBrandEntity> findAll(Predicate predicate);
}
