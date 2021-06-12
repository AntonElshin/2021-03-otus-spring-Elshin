package ru.otus.homework.service;

import ru.otus.homework.domain.Book;

import java.util.List;

public interface PrintBookService {

    String printBook(Book book);

    String printBooks(List<Book> books);

    String printBooksCount(Long count);

    String prepareBook(Book book, Boolean commentsFlag);

}
