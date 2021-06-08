package ru.otus.homework.service;

import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDTO;
import ru.otus.homework.dto.BookResDTO;
import ru.otus.homework.dto.BookWithCommentsDTO;

import java.util.List;

public interface BookService {

    BookDTO add(BookDTO bookDTO);

    BookDTO update(long id, BookDTO bookDTO);

    BookWithCommentsDTO getById(long id);

    void deleteById(long id);

    List<BookResDTO> findAll();

    List<BookResDTO> findByParams(String title, String isbn, Long authorId, Long genreId);

    Book processLinks(BookDTO bookDTO);

}
