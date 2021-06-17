package ru.otus.homework.repository;

import ru.otus.homework.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepositoryJpa {

    Long countByBookId(long bookId);

    BookComment save(BookComment bookComment);

    Optional<BookComment> findById(long id);

    void delete(BookComment bookComment);

}
