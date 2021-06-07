package ru.otus.homework.service;

import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.dto.BookCommentResDTO;

import java.util.List;

public interface BookCommentService {

    BookCommentResDTO add(BookCommentDTO bookCommentDTO);

    BookCommentResDTO update(long id, BookCommentDTO bookCommentDTO);

    BookCommentResDTO getById(long id);

    void deleteById(long id);

    List<BookCommentResDTO> findAllByBookId(long bookId);

}
