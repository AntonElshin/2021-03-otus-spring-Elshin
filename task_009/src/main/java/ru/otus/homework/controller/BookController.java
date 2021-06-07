package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.BookDTO;
import ru.otus.homework.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/api/books/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookDTO> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookDTO> findBooksByParams(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "authorId", required = false) Long authorId,
            @RequestParam(value = "genreId", required = false) Long genreId
    ) {
        return bookService.findByParams(title, isbn, authorId, genreId);
    }

    @GetMapping(value = "/api/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO getBook (
            @PathVariable(name = "id") Long id
    ) {
        return bookService.getById(id);
    }

    @PostMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO createBook (
            @RequestBody BookDTO bookDTO
    ) {
        return bookService.add(bookDTO);
    }

    @PutMapping(path = "/api/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO modifyBook (
            @PathVariable(name = "id") Long id,
            @RequestBody BookDTO bookDTO
    ) {
        return bookService.update(id, bookDTO);
    }

    @DeleteMapping(value = "/api/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteBook (
            @PathVariable(name = "id") Long id
    ) {
        bookService.deleteById(id);
    }

}
