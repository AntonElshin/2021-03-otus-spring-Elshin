package ru.otus.homework.service;

import ru.otus.homework.domain.BookComment;

public interface BookCommentService {

    BookComment add(Long bookId, String text);

    void update(long id, Long bookId, String text);

    void getById(long id);

    void deleteById(long id);

    void findAllByBookId(long bookId);

    void countByBookId(long bookId);

}
