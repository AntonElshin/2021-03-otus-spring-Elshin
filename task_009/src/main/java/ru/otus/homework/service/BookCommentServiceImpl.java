package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.BookComment;
import ru.otus.homework.dto.BookCommentReqDTO;
import ru.otus.homework.dto.BookCommentResDTO;
import ru.otus.homework.dto.BookCommentResListDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mapper.BookCommentMapper;
import ru.otus.homework.repository.BookCommentRepository;
import ru.otus.homework.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;
    private final BookRepository bookRepository;
    private final BookCommentMapper bookCommentMapper;

    @Override
    @Transactional
    public BookCommentResDTO add(BookCommentReqDTO bookCommentReqDTO) {

        if(bookCommentReqDTO.getBook() != null && bookCommentReqDTO.getBook().getId() == null) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Long bookId = bookCommentReqDTO.getBook().getId();

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));

        BookComment bookComment = new BookComment(book, bookCommentReqDTO.getText());
        bookComment = bookCommentRepository.save(bookComment);
        return bookCommentMapper.toDto(bookComment);
    }

    @Override
    @Transactional
    public BookCommentResDTO update(long id, BookCommentReqDTO bookCommentReqDTO) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        BookComment bookComment = bookCommentRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id));
        Book book = bookComment.getBook();

        if(bookCommentReqDTO.getBook() != null && bookCommentReqDTO.getBook().getId() != null) {
            Long bookId = bookCommentReqDTO.getBook().getId();
            book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));
        }

        BookComment updateBookComment = bookCommentMapper.fromDto(bookCommentReqDTO);
        updateBookComment.setId(id);
        updateBookComment.setBook(book);

        bookCommentRepository.save(updateBookComment);
        return bookCommentMapper.toDto(updateBookComment);
    }

    @Override
    @Transactional(readOnly = true)
    public BookCommentResDTO getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        BookComment bookComment = bookCommentRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id));
        return bookCommentMapper.toDto(bookComment);

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        bookCommentRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id));
        bookCommentRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCommentResListDTO> findAllByBookId(long bookId) {

        if(bookId == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));
        return bookCommentMapper.toListDto(book.getComments());

    }

}
