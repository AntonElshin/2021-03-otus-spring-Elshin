package ru.otus.homework.library.service;

import ru.otus.homework.library.domain.Book;
import ru.otus.homework.library.dto.BookReqDTO;
import ru.otus.homework.library.dto.BookResListDTO;
import ru.otus.homework.library.dto.BookResDTO;
import ru.otus.homework.library.dto.BookResWithCommentsDTO;

import java.util.List;

public interface BookService {

    BookResDTO add(BookReqDTO bookReqDTO);

    BookResDTO update(long id, BookReqDTO bookReqDTO);

    BookResWithCommentsDTO getById(long id);

    void deleteById(long id);

    List<BookResListDTO> findAll();

    List<BookResListDTO> findByParams(String title, String isbn, Long authorId, Long genreId);

    Book processLinks(BookReqDTO bookReqDTO);

}
