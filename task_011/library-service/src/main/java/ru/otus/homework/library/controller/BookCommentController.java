package ru.otus.homework.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.library.api.BookCommentApi;
import ru.otus.homework.library.dto.BookCommentReqDTO;
import ru.otus.homework.library.dto.BookCommentResDTO;
import ru.otus.homework.library.dto.BookCommentResListDTO;
import ru.otus.homework.library.service.BookCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookCommentController implements BookCommentApi {

    private final BookCommentService bookCommentService;

    @Override
    public ResponseEntity<List<BookCommentResListDTO>> findBookCommentsByParams(Long commentBookId) {
        return ResponseEntity.ok(bookCommentService.findAllByBookId(commentBookId));
    }

    @Override
    public ResponseEntity<BookCommentResDTO> getBookComment(Long bookCommentId) {
        return ResponseEntity.ok(bookCommentService.getById(bookCommentId));
    }

    @Override
    public ResponseEntity<BookCommentResDTO> createBookComment(BookCommentReqDTO bookCommentReqDTO) {
        return ResponseEntity.ok(bookCommentService.add(bookCommentReqDTO));
    }

    @Override
    public ResponseEntity<BookCommentResDTO> modifyBookComment(Long bookCommentId, BookCommentReqDTO bookCommentReqDTO) {
        return ResponseEntity.ok(bookCommentService.update(bookCommentId, bookCommentReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteBookComment(Long bookCommentId) {
        bookCommentService.deleteById(bookCommentId);
        return ResponseEntity.ok(null);
    }

}
