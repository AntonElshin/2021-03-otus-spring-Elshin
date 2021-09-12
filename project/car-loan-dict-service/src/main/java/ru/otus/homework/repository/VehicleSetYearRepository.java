package ru.otus.homework.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.homework.entity.QVehicleSetYearEntity;
import ru.otus.homework.entity.VehicleSetYearEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleSetYearRepository extends PagingAndSortingRepository<VehicleSetYearEntity, Long>, QuerydslPredicateExecutor<VehicleSetYearEntity>,
        QuerydslBinderCustomizer<QVehicleSetYearEntity> {

    @Override
    default void customize(QuerydslBindings bindings, QVehicleSetYearEntity qEntity) {
        bindings.bind(qEntity.set.id)
                .first(SimpleExpression::eq);
        bindings.bind(qEntity.year.id)
                .first(SimpleExpression::eq);
    }

    Optional<VehicleSetYearEntity> findOne(Predicate predicate);

    List<VehicleSetYearEntity> findAll(Predicate predicate);

    List<VehicleSetYearEntity> findAll();

    @Modifying
    @Query("delete from VehicleSetYearEntity")
    void deleteAllVehicleSetYears();

}
