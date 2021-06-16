package ru.otus.homework.library.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ru.otus.homework.library.domain.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long>, QuerydslPredicateExecutor<Author> {

    List<Author> findAll(Predicate predicate);

    @Query("select a from Author a where a.id in :ids")
    List<Author> findByIds(@Param("ids") List<Long> authorIds);

}
