package ru.otus.homework.service;

import ru.otus.homework.dto.BookCommentReqDTO;
import ru.otus.homework.dto.BookCommentResDTO;
import ru.otus.homework.dto.BookCommentResListDTO;

import java.util.List;

public interface BookCommentService {

    BookCommentResDTO add(BookCommentReqDTO bookCommentReqDTO);

    BookCommentResDTO update(long id, BookCommentReqDTO bookCommentReqDTO);

    BookCommentResDTO getById(long id);

    void deleteById(long id);

    List<BookCommentResListDTO> findAllByBookId(long bookId);

}
