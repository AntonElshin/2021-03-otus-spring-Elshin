package ru.otus.homework.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.library.api.BookApi;
import ru.otus.homework.library.dto.BookReqDTO;
import ru.otus.homework.library.dto.BookResDTO;
import ru.otus.homework.library.dto.BookResListDTO;
import ru.otus.homework.library.dto.BookResWithCommentsDTO;
import ru.otus.homework.library.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {

    private final BookService bookService;

    @Override
    public ResponseEntity<List<BookResListDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @Override
    public ResponseEntity<List<BookResListDTO>> findBooksByParams(String bookTitle, String bookISBN, Long bookAuthorId, Long bookGenreId) {
        return ResponseEntity.ok(bookService.findByParams(bookTitle, bookISBN, bookAuthorId, bookGenreId));
    }

    @Override
    public ResponseEntity<BookResWithCommentsDTO> getBook(Long bookId) {
        return ResponseEntity.ok(bookService.getById(bookId));
    }

    @Override
    public ResponseEntity<BookResDTO> createBook(BookReqDTO bookReqDTO) {
        return ResponseEntity.ok(bookService.add(bookReqDTO));
    }

    @Override
    public ResponseEntity<BookResDTO> modifyBook(Long bookId, BookReqDTO bookReqDTO) {
        return ResponseEntity.ok(bookService.update(bookId, bookReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long bookId) {
        bookService.deleteById(bookId);
        return ResponseEntity.ok(null);
    }

}
