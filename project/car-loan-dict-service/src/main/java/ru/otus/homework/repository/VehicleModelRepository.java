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
import ru.otus.homework.entity.QVehicleModelEntity;
import ru.otus.homework.entity.VehicleModelEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleModelRepository extends PagingAndSortingRepository<VehicleModelEntity, Long>, QuerydslPredicateExecutor<VehicleModelEntity>,
        QuerydslBinderCustomizer<QVehicleModelEntity> {

    @Override
    default void customize(QuerydslBindings bindings, QVehicleModelEntity qEntity) {
        bindings.bind(qEntity.brand.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.name)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(qEntity.kind.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.ownForms.any().id)
                .first(SimpleExpression::eq);
    }

    Optional<VehicleModelEntity> findOne(Predicate predicate);

    List<VehicleModelEntity> findAll(Predicate predicate);

    List<VehicleModelEntity> findAll();

    void deleteByBrand_Id(Long vehicleBrandId);

    List<VehicleModelEntity> findAllByBrand_Id(Long vehicleBrandId);
}
