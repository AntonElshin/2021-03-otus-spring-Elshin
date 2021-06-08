package ru.otus.homework.repository;

import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryJpa {

    Long count();

    Genre save(Genre genre);

    Optional<Genre> findById(long id);

    void delete(Genre genre);

    List<Genre> findAll();

    List<Genre> findByParamsEqualsIgnoreCase(String name);

    List<Genre> findByParamsLikeIgnoreCase(String name);

    List<Genre> findByIds(List<Long> genreIds);

}
