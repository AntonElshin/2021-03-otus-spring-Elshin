package ru.otus.homework.service;

import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookReqDTO;
import ru.otus.homework.dto.BookResListDTO;
import ru.otus.homework.dto.BookResDTO;
import ru.otus.homework.dto.BookResWithCommentsDTO;

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
