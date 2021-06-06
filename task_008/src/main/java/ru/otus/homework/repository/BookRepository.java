package ru.otus.homework.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.otus.homework.domain.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {

    @EntityGraph(attributePaths = "comments")
    List<Book> findAll(Predicate predicate);

    @EntityGraph(attributePaths = "comments")
    List<Book> findAll();

}
