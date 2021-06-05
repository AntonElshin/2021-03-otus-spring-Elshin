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
    private final PrintService printService;

    @Override
    @Transactional
    public BookComment add(Long bookId, String text) {

        if(bookId == null) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }
        if(text == null || (text != null && text.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_TEXT);
        }

        Optional<Book> foundBook = bookRepositoryJpa.findById(bookId);

        if(!foundBook.isPresent()) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId);
        }

        BookComment bookComment = new BookComment(foundBook.get(), text);
        bookCommentRepositoryJpa.save(bookComment);
        return bookComment;
    }

    @Override
    @Transactional
    public void update(long id, Long bookId, String text) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        Optional<BookComment> foundBookComment = bookCommentRepositoryJpa.findById(id);

        if(!foundBookComment.isPresent()) {
            throw new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id);
        }

        BookComment bookComment = foundBookComment.get();

        if(bookId != null) {
            Optional<Book> foundBook = bookRepositoryJpa.findById(bookId);

            if (!foundBook.isPresent()) {
                throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId);
            }
            bookComment.setBook(foundBook.get());
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

        Optional<BookComment> foundBookComment = bookCommentRepositoryJpa.findById(id);

        if(!foundBookComment.isPresent()) {
            throw new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id);
        }

        BookComment bookComment = foundBookComment.get();

        printService.printBookComment(bookComment);

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        Optional<BookComment> foundBookComment = bookCommentRepositoryJpa.findById(id);

        if(!foundBookComment.isPresent()) {
            throw new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id);
        }

        bookCommentRepositoryJpa.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public void findAllByBookId(long bookId) {

        if(bookId == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Optional<Book> foundBook = bookRepositoryJpa.findById(bookId);

        if (!foundBook.isPresent()) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId);
        }

        List<BookComment> bookComments = bookCommentRepositoryJpa.findAllByBookId(bookId);
        printService.printBookComments(bookComments);
    }

    @Override
    @Transactional(readOnly = true)
    public void countByBookId(long bookId) {

        if(bookId == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Optional<Book> foundBook = bookRepositoryJpa.findById(bookId);

        if (!foundBook.isPresent()) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId);
        }

        printService.printBookCommentsCount(bookCommentRepositoryJpa.countByBookId(bookId));
    }
}
