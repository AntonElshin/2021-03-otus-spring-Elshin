package ru.otus.homework.service;

import ru.otus.homework.domain.Author;

import java.util.List;

public interface PrintAuthorService {

    String printAuthor(Author author);

    String printAuthors(List<Author> authors);

    String printAuthorsCount(Long count);

    String prepareAuthor(Author author);

}
