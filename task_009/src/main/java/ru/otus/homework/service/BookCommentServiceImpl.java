package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.BookComment;
import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.dto.BookCommentResDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mapper.BookCommentMapper;
import ru.otus.homework.repository.BookCommentRepository;
import ru.otus.homework.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;
    private final BookRepository bookRepository;
    private final ValidationService validationService;

    @Override
    @Transactional
    public BookCommentResDTO add(BookCommentDTO bookCommentDTO) {

        validationService.validateDTO(bookCommentDTO);

        if(bookCommentDTO.getBook() != null && bookCommentDTO.getBook().getId() == null) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Long bookId = bookCommentDTO.getBook().getId();

        Optional<Book> foundBook = bookRepository.findById(bookId);

        if(!foundBook.isPresent()) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId);
        }

        BookComment bookComment = new BookComment(foundBook.get(), bookCommentDTO.getText());
        bookCommentRepository.save(bookComment);
        BookCommentResDTO bookCommentResDTO = new BookCommentResDTO(bookComment.getId(), bookComment.getText());
        return bookCommentResDTO;
    }

    @Override
    @Transactional
    public BookCommentResDTO update(long id, BookCommentDTO bookCommentDTO) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        validationService.validateDTO(bookCommentDTO);

        Optional<BookComment> foundBookComment = bookCommentRepository.findById(id);

        if(!foundBookComment.isPresent()) {
            throw new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id);
        }

        BookComment bookComment = foundBookComment.get();
        Book book = bookComment.getBook();

        if(bookCommentDTO.getBook() != null && bookCommentDTO.getBook().getId() != null) {
            Long bookId = bookCommentDTO.getBook().getId();

            Optional<Book> foundBook = bookRepository.findById(bookId);

            if (!foundBook.isPresent()) {
                throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId);
            }

            book = foundBook.get();
        }

        BookComment updateBookComment = BookCommentMapper.INSTANCE.fromDto(bookCommentDTO);
        updateBookComment.setId(id);
        updateBookComment.setBook(book);

        bookCommentRepository.save(updateBookComment);
        BookCommentResDTO bookCommentResDTO = new BookCommentResDTO(updateBookComment.getId(), updateBookComment.getText());
        return bookCommentResDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public BookCommentResDTO getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        Optional<BookComment> foundBookComment = bookCommentRepository.findById(id);

        if(!foundBookComment.isPresent()) {
            throw new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id);
        }

        BookComment bookComment = foundBookComment.get();
        BookCommentResDTO bookCommentResDTO = new BookCommentResDTO(bookComment.getId(), bookComment.getText());
        return bookCommentResDTO;

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        Optional<BookComment> foundBookComment = bookCommentRepository.findById(id);

        if(!foundBookComment.isPresent()) {
            throw new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id);
        }

        bookCommentRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCommentResDTO> findAllByBookId(long bookId) {

        if(bookId == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Optional<Book> foundBook = bookRepository.findById(bookId);

        if (!foundBook.isPresent()) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId);
        }

        List<BookComment> bookComments = bookCommentRepository.findAllByBookId(bookId);
        List<BookCommentResDTO> bookCommentsResDTO = new ArrayList<>();
        if(bookCommentsResDTO != null) {
            for(BookComment bookComment : bookComments) {
                BookCommentResDTO bookCommentResDTO = new BookCommentResDTO(bookComment.getId(), bookComment.getText());
                bookCommentsResDTO.add(bookCommentResDTO);
            }
        }

        return bookCommentsResDTO;
    }

}
