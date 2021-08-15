package ru.otus.homework.library.service;

import ru.otus.homework.library.dto.BookCommentReqDTO;
import ru.otus.homework.library.dto.BookCommentResDTO;
import ru.otus.homework.library.dto.BookCommentResListDTO;

import java.util.List;

public interface BookCommentService {

    BookCommentResDTO add(BookCommentReqDTO bookCommentReqDTO);

    BookCommentResDTO update(long id, BookCommentReqDTO bookCommentReqDTO);

    BookCommentResDTO getById(long id);

    void deleteById(long id);

    List<BookCommentResListDTO> findAllByBookId(long bookId);

}
