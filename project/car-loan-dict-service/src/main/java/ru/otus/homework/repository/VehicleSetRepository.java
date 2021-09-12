package ru.otus.homework.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.homework.entity.QVehicleSetEntity;
import ru.otus.homework.entity.VehicleSetEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleSetRepository extends PagingAndSortingRepository<VehicleSetEntity, Long>, QuerydslPredicateExecutor<VehicleSetEntity>,
        QuerydslBinderCustomizer<QVehicleSetEntity> {

    @Override
    default void customize(QuerydslBindings bindings, QVehicleSetEntity qEntity) {
        bindings.bind(qEntity.model.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.body.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.engineSize.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.engineType.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.power.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.transmission.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.years.any().id)
                .first(SimpleExpression::eq);
    }

    Optional<VehicleSetEntity> findOne(Predicate predicate);

    List<VehicleSetEntity> findAll(Predicate predicate);

    List<VehicleSetEntity> findAll();

    void deleteByModel_Id(Long vehicleModelId);

    @Modifying
    @Query("delete from VehicleSetEntity")
    void deleteAllVehicleSets();

}
