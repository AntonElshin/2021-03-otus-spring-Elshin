package ru.otus.homework.dao;

import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Genre;

import java.util.List;

public interface AuthorDao {

    int count();

    void insert(long id, Author author);

    void update(long id, Author author);

    Author getById(long id);

    void deleteById(long id);

    List<Author> getAll();

    List<Author> getByParamsEqualsIgnoreCase(String lastName, String firstName, String middleName);

    List<Author> getByParamsLikeIgnoreCase(String lastName, String firstName, String middleName);

    List<Author> getByIds(List<String> authorIds);

    Integer getLinkedBookCount(long id);

}
