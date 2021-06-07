package ru.otus.homework.service;

import ru.otus.homework.dto.BookDTO;

import java.util.List;

public interface BookService {

    BookDTO add(BookDTO bookDTO);

    BookDTO update(long id, BookDTO bookDTO);

    BookDTO getById(long id);

    void deleteById(long id);

    List<BookDTO> findAll();

    List<BookDTO> findByParams(String title, String isbn, Long authorId, Long genreId);

}
