package ru.otus.homework.service;

import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.dto.BookCommentIdTextDTO;

import java.util.List;

public interface BookCommentService {

    BookCommentIdTextDTO add(BookCommentDTO bookCommentDTO);

    BookCommentIdTextDTO update(long id, BookCommentDTO bookCommentDTO);

    BookCommentIdTextDTO getById(long id);

    void deleteById(long id);

    List<BookCommentIdTextDTO> findAllByBookId(long bookId);

}
