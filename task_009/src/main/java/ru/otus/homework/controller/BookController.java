package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.BookResListDTO;
import ru.otus.homework.dto.BookReqDTO;
import ru.otus.homework.dto.BookResDTO;
import ru.otus.homework.dto.BookResWithCommentsDTO;
import ru.otus.homework.service.BookService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/api/books/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookResListDTO> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookResListDTO> findBooksByParams(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "authorId", required = false) Long authorId,
            @RequestParam(value = "genreId", required = false) Long genreId
    ) {
        return bookService.findByParams(title, isbn, authorId, genreId);
    }

    @GetMapping(value = "/api/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookResWithCommentsDTO getBook (
            @PathVariable(name = "id") Long id
    ) {
        return bookService.getById(id);
    }

    @PostMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookResDTO createBook (
            @Valid @RequestBody BookReqDTO bookReqDTO
    ) {
        return bookService.add(bookReqDTO);
    }

    @PutMapping(path = "/api/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookResDTO modifyBook (
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody BookReqDTO bookReqDTO
    ) {
        return bookService.update(id, bookReqDTO);
    }

    @DeleteMapping(value = "/api/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteBook (
            @PathVariable(name = "id") Long id
    ) {
        bookService.deleteById(id);
    }

}
