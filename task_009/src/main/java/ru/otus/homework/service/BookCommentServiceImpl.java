package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.BookComment;
import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.dto.BookCommentIdTextDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mapper.BookCommentIdTextMapper;
import ru.otus.homework.mapper.BookCommentMapper;
import ru.otus.homework.repository.BookCommentRepository;
import ru.otus.homework.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public BookCommentIdTextDTO add(BookCommentDTO bookCommentDTO) {

        if(bookCommentDTO.getBook() != null && bookCommentDTO.getBook().getId() == null) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Long bookId = bookCommentDTO.getBook().getId();

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));

        BookComment bookComment = new BookComment(book, bookCommentDTO.getText());
        bookCommentRepository.save(bookComment);
        return BookCommentIdTextMapper.INSTANCE.toDto(bookComment);
    }

    @Override
    @Transactional
    public BookCommentIdTextDTO update(long id, BookCommentDTO bookCommentDTO) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        BookComment bookComment = bookCommentRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id));
        Book book = bookComment.getBook();

        if(bookCommentDTO.getBook() != null && bookCommentDTO.getBook().getId() != null) {
            Long bookId = bookCommentDTO.getBook().getId();
            book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));
        }

        BookComment updateBookComment = BookCommentMapper.INSTANCE.fromDto(bookCommentDTO);
        updateBookComment.setId(id);
        updateBookComment.setBook(book);

        bookCommentRepository.save(updateBookComment);
        return BookCommentIdTextMapper.INSTANCE.toDto(updateBookComment);
    }

    @Override
    @Transactional(readOnly = true)
    public BookCommentIdTextDTO getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_COMMENT_ID);
        }

        BookComment bookComment = bookCommentRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_COMMENT_NOT_FOUND_BY_ID, id));
        return BookCommentIdTextMapper.INSTANCE.toDto(bookComment);

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
    public List<BookCommentIdTextDTO> findAllByBookId(long bookId) {

        if(bookId == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, bookId));
        return BookCommentIdTextMapper.INSTANCE.toListDto(book.getComments());

    }

}
