package ru.otus.homework.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.homework.library.domain.BookComment;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    @Query("select count(bc) from BookComment bc where bc.book.id = :bookId")
    Long countByBookId(@Param("bookId") Long bookId);

}
