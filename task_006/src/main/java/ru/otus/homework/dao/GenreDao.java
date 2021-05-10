package ru.otus.homework.dao;

import ru.otus.homework.domain.Genre;

import java.util.List;

public interface GenreDao {

    int count();

    void insert(long id, Genre genre);

    void update(long id, Genre genre);

    Genre getById(long id);

    void deleteById(long id);

    List<Genre> getAll();

    List<Genre> getByParamsEqualsIgnoreCase(String name);

    List<Genre> getByParamsLikeIgnoreCase(String name);

    List<Genre> getByIds(List<String> genreIds);

    Integer getLinkedBookCount(long id);

}
