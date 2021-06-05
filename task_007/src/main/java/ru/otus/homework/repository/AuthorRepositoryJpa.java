package ru.otus.homework.repository;

import ru.otus.homework.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryJpa {

    Long count();

    Author save(Author author);

    Optional<Author> findById(long id);

    void deleteById(long id);

    List<Author> findAll();

    List<Author> findByParamsEqualsIgnoreCase(String lastName, String firstName, String middleName);

    List<Author> findByParamsLikeIgnoreCase(String lastName, String firstName, String middleName);

    List<Author> findByIds(List<Long> authorIds);

}
