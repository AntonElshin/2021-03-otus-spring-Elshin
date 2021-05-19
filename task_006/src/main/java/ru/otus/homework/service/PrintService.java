package ru.otus.homework.service;

import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;

public interface PrintService {

    String printGenre(Genre genre);

    String printGenres(List<Genre> genres);

    String printGenresCount(int count);

    String printAuthor(Author author);

    String printAuthors(List<Author> authors);

    String printAuthorsCount(int count);

    String printBook(Book book);

    String printBooks(List<Book> books);

    String printBooksCount(int count);

}
