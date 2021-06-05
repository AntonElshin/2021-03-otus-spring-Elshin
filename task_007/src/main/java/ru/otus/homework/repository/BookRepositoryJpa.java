package ru.otus.homework.repository;

import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryJpa {

    Long count();

    Book save(Book book);

    Optional<Book> findById(long id);

    void deleteById(long id);

    List<Book> findAll();

    List<Book> findByParamsEqualsIgnoreCase(String title, String isbn, Long authorId, Long genreId);

    List<Book> findByParamsLikeIgnoreCase(String title, String isbn, Long authorId, Long genreId);

}
