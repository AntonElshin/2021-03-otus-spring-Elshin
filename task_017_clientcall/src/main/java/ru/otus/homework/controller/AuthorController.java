package ru.otus.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.library.dto.AuthorResListDTO;
import ru.otus.homework.service.AuthorService;

import java.util.List;

@RestController
public class AuthorController {

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/api/authors/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuthorResListDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.findAll());
    }
}
