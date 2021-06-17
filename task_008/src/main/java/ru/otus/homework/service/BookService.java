package ru.otus.homework.service;

import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;

public interface BookService {

    Book add(String title, String isbn, String description, String authorStr, String genreStr);

    void update(long id, String title, String isbn, String description, String authorStr, String genreStr);

    void getById(long id);

    void deleteById(long id);

    void findAll();

    void findByParams(String title, String isbn, Long authorId, Long genreId);

    void count();

}
