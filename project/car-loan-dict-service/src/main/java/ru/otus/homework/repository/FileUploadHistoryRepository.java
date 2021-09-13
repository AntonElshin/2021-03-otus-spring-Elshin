package ru.otus.homework.repository;

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
import ru.otus.homework.entity.FileUploadHistoryEntity;
import ru.otus.homework.entity.QFileUploadHistoryEntity;

public interface FileUploadHistoryRepository extends PagingAndSortingRepository<FileUploadHistoryEntity, Long>, QuerydslPredicateExecutor<FileUploadHistoryEntity>,
        QuerydslBinderCustomizer<QFileUploadHistoryEntity> {

    @Override
    default void customize(QuerydslBindings bindings, QFileUploadHistoryEntity qEntity) {
        bindings.bind(qEntity.fileType.id)
                .first(SimpleExpression::eq);
    }

    @Query("select max(versionNumber) from FileUploadHistoryEntity where fileType.id = :fileTypeId")
    Integer getMaxVersionNumberByFileTypeId(@Param(value = "fileTypeId") Long fileTypeId);


}
