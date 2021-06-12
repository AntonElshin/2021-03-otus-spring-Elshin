package ru.otus.homework.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.library.api.AuthorApi;
import ru.otus.homework.library.dto.AuthorReqDTO;
import ru.otus.homework.library.dto.AuthorResDTO;
import ru.otus.homework.library.dto.AuthorResListDTO;
import ru.otus.homework.library.service.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController implements AuthorApi {

    private final AuthorService authorService;

    @Override
    public ResponseEntity<List<AuthorResListDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.findAll());
    }

    @Override
    public ResponseEntity<List<AuthorResListDTO>> findAuthorsByParams(String authorLastName, String authorFirstName, String authorMiddleName) {
        return ResponseEntity.ok(authorService.findByParams(authorLastName, authorFirstName, authorMiddleName));
    }

    @Override
    public ResponseEntity<AuthorResDTO> getAuthor(Long authorId) {
        return ResponseEntity.ok(authorService.getById(authorId));
    }

    @Override
    public ResponseEntity<AuthorResDTO> createAuthor(AuthorReqDTO authorReqDTO) {
        return ResponseEntity.ok(authorService.add(authorReqDTO));
    }

    @Override
    public ResponseEntity<AuthorResDTO> modifyAuthor(Long authorId, AuthorReqDTO authorReqDTO) {
        return ResponseEntity.ok(authorService.update(authorId, authorReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteAuthor(Long authorId) {
        authorService.deleteById(authorId);
        return ResponseEntity.ok(null);
    }

}
