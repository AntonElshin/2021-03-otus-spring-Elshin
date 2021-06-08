package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.dto.BookCommentIdTextDTO;
import ru.otus.homework.service.BookCommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookCommentController {

    private final BookCommentService bookCommentService;

    @GetMapping(value = "/api/bookcomments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookCommentIdTextDTO> findBookCommentsByParams(
            @RequestParam(value = "bookId") Long bookId
    ) {
        return bookCommentService.findAllByBookId(bookId);
    }

    @GetMapping(value = "/api/bookcomments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookCommentIdTextDTO getBookComment (
            @PathVariable(name = "id") Long id
    ) {
        return bookCommentService.getById(id);
    }

    @PostMapping(value = "/api/bookcomments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookCommentIdTextDTO createBookComment (
            @Valid @RequestBody BookCommentDTO bookCommentDTO
    ) {
        return bookCommentService.add(bookCommentDTO);
    }

    @PutMapping(path = "/api/bookcomments/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookCommentIdTextDTO modifyBookComment (
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody BookCommentDTO bookCommentDTO
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
