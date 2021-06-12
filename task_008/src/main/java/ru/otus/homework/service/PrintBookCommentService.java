package ru.otus.homework.service;

import ru.otus.homework.domain.BookComment;

import java.util.List;

public interface PrintBookCommentService {

    String printBookComment(BookComment bookComment);

    String printBookComments(List<BookComment> bookComments);

    String printBookCommentsCount(Long count);

    String prepareBookComment(BookComment bookComment);

}
