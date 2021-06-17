package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.BookComment;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.repository.BookCommentRepositoryJpa;
import ru.otus.homework.repository.BookRepositoryJpa;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepositoryJpa bookCommentRepositoryJpa;
    private final BookRepositoryJpa bookRepositoryJpa;
    private final PrintBookCommentService printBookCommentService;

    @Override
    @Transactional
    public BookComment add(Long bookId, String text) {

        if(bookId == null) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }
        if(text == null || (text != null && text.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_TEXT);
        }

        Book book = bookRepositoryJpa.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));

        BookComment bookComment = new BookComment(book, text);
        bookCommentRepositoryJpa.save(bookComment);
        return bookComment;
    }

    @Override
    @Transactional
    public void update(long id, Long bookId, String text) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        BookComment bookComment = bookCommentRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id));

        if(bookId != null) {
            Book book = bookRepositoryJpa.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));
            bookComment.setBook(book);
        }

        if(text != null && !text.trim().isEmpty()) {
            bookComment.setText(text);
        }

        bookCommentRepositoryJpa.save(bookComment);

    }

    @Override
    @Transactional(readOnly = true)
    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        BookComment bookComment = bookCommentRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id));
        printBookCommentService.printBookComment(bookComment);

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        BookComment bookComment = bookCommentRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id));
        bookCommentRepositoryJpa.delete(bookComment);

    }

    @Override
    @Transactional(readOnly = true)
    public void findAllByBookId(long bookId) {

        if(bookId == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookRepositoryJpa.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));

        List<BookComment> bookComments = book.getComments();
        printBookCommentService.printBookComments(bookComments);
    }

    @Override
    @Transactional(readOnly = true)
    public void countByBookId(long bookId) {

        if(bookId == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookRepositoryJpa.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));

        printBookCommentService.printBookCommentsCount(bookCommentRepositoryJpa.countByBookId(bookId));
    }
}
