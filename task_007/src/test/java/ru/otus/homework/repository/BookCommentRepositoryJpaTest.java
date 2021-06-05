package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.BookComment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с книгами должно")
@DataJpaTest
@Import({BookCommentRepositoryJpaImpl.class})
public class BookCommentRepositoryJpaTest {

    private static final int EXPECTED_BOOK_COMMENTS_COUNT = 2;

    private static final int EXISTING_BOOK_COMMENT_ID = 1;
    private static final String EXISTING_BOOK_COMMENT_TEXT = "Неожиданная концовка!";

    private static final int EXISTING_BOOK_ID = 1;

    @Autowired
    private BookCommentRepositoryJpaImpl bookCommentRepositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("возвращать ожидаемое количество комментариев к книге")
    @Test
    void shouldReturnExpectedBookCommentsCount() {
        Long actualBookCommentsCount = bookCommentRepositoryJpa.countByBookId(EXISTING_BOOK_ID);
        assertThat(actualBookCommentsCount).isEqualTo(EXPECTED_BOOK_COMMENTS_COUNT);
    }

    @DisplayName("добавлять комментарий к книге в БД")
    @Test
    void shouldInsertBookComment() {

        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        em.persist(createdBook);

        BookComment expectedBookComment = new BookComment(createdBook, "Очень интересно!");
        bookCommentRepositoryJpa.save(expectedBookComment);

        BookComment actualBookComment = em.find(BookComment.class, expectedBookComment.getId());
        assertThat(actualBookComment).isNotNull();
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(expectedBookComment);
    }

    @DisplayName("изменять комментарий к книге в БД")
    @Test
    void shouldUpdateBookComment() {

        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        em.persist(createdBook);

        BookComment expectedBookComment = new BookComment(createdBook, "Очень интересно!");
        Long bookCommentId = em.persistAndGetId(expectedBookComment, Long.class);

        expectedBookComment = new BookComment(bookCommentId, createdBook, "Очень интересно!!!");
        bookCommentRepositoryJpa.save(expectedBookComment);

        BookComment actualBookComment = em.find(BookComment.class, bookCommentId);
        assertThat(actualBookComment).isNotNull();
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(expectedBookComment);

    }

    @DisplayName("возвращать ожидаемый комментарий к книге по его id")
    @Test
    void shouldReturnExpectedBookCommentById() {

        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        em.persist(createdBook);

        BookComment createdBookComment = new BookComment(createdBook, "Очень интересно!");
        Long bookCommentId = em.persistAndGetId(createdBookComment, Long.class);

        Optional<BookComment> expectedBookComment = bookCommentRepositoryJpa.findById(bookCommentId);

        BookComment actualBookComment = em.find(BookComment.class, bookCommentId);
        assertThat(actualBookComment).isNotNull();
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(expectedBookComment.get());

    }

    @DisplayName("удалять заданного комментарий к книге по его id")
    @Test
    void shouldCorrectDeleteBookCommentById() {

        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        em.persist(createdBook);

        BookComment expectedBookComment = new BookComment(createdBook, "Очень интересно!");
        Long bookCommentId = em.persistAndGetId(expectedBookComment, Long.class);

        assertThat(bookCommentId).isNotEqualTo(0);

        bookCommentRepositoryJpa.deleteById(bookCommentId);
        em.flush();
        em.detach(expectedBookComment);

        BookComment bookComment = em.find(BookComment.class, bookCommentId);
        assertThat(bookComment).isNull();

    }

    @DisplayName("вернуть всех комментарий к книге по идентификатору книгу")
    @Test
    void shouldFindAllBookCommentsByBookId() {

        List<BookComment> bookComments = bookCommentRepositoryJpa.findAllByBookId(EXISTING_BOOK_ID);

        assertThat(bookComments).hasSize(2);

        assertThat(bookComments.get(0).getId()).isEqualTo(1L);
        assertThat(bookComments.get(0).getText()).containsIgnoringCase("Увлекательный детектив!");

        assertThat(bookComments.get(1).getId()).isEqualTo(2L);
        assertThat(bookComments.get(1).getText()).containsIgnoringCase("Неожиданная концовка!");

    }



}
