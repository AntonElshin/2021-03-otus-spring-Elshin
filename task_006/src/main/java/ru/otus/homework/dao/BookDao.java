package ru.otus.homework.dao;

import ru.otus.homework.dao.ext.BookAuthor;
import ru.otus.homework.dao.ext.BookGenre;
import ru.otus.homework.domain.Book;

import java.util.List;

public interface BookDao {

    int count();

    void insert(long id, Book book);

    void update(long id, Book book);

    Book getById(long id);

    void deleteById(long id);

    List<Book> getAll();

    List<Book> getByParamsEqualsIgnoreCase(String title, String isbn, Long authorId, Long genreId);

    List<Book> getByParamsLikeIgnoreCase(String title, String isbn, Long authorId, Long genreId);

    List<Book> getBooksLinks(List<Book> books);

    List<BookAuthor> getBookAuthors(List<String> bookIds);

    List<BookGenre> getBookGenres(List<String> bookIds);

}
