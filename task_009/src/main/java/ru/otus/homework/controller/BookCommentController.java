package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.dto.BookCommentResDTO;
import ru.otus.homework.service.BookCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookCommentController {

    private final BookCommentService bookCommentService;

    @GetMapping(value = "/api/bookcomments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookCommentResDTO> findBookCommentsByParams(
            @RequestParam(value = "bookId") Long bookId
    ) {
        return bookCommentService.findAllByBookId(bookId);
    }

    @GetMapping(value = "/api/bookcomments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookCommentResDTO getBookComment (
            @PathVariable(name = "id") Long id
    ) {
        return bookCommentService.getById(id);
    }

    @PostMapping(value = "/api/bookcomments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookCommentResDTO createBookComment (
            @RequestBody BookCommentDTO bookCommentDTO
    ) {
        return bookCommentService.add(bookCommentDTO);
    }

    @PutMapping(path = "/api/bookcomments/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookCommentResDTO modifyBookComment (
            @PathVariable(name = "id") Long id,
            @RequestBody BookCommentDTO bookCommentDTO
    ) {
        return bookCommentService.update(id, bookCommentDTO);
    }

    @DeleteMapping(value = "/api/bookcomments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteBookComment (
            @PathVariable(name = "id") Long id
    ) {
        bookCommentService.deleteById(id);
    }

}
