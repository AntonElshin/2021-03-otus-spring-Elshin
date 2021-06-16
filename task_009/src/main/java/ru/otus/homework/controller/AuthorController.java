package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.AuthorReqDTO;
import ru.otus.homework.dto.AuthorResDTO;
import ru.otus.homework.dto.AuthorResListDTO;
import ru.otus.homework.service.AuthorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping(value = "/api/authors/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorResListDTO> getAllAuthors() {
        return authorService.findAll();
    }

    @GetMapping(value = "/api/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorResListDTO> findAuthorsByParams(
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "middleName", required = false) String middleName
    ) {
        return authorService.findByParams(lastName, firstName, middleName);
    }

    @GetMapping(value = "/api/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorResDTO getAuthor (
            @PathVariable(name = "id") Long id
    ) {
        return authorService.getById(id);
    }

    @PostMapping(value = "/api/authors", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorResDTO createAuthor (
            @Valid @RequestBody AuthorReqDTO authorReqDTO
    ) {
        return authorService.add(authorReqDTO);
    }

    @PutMapping(path = "/api/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorResDTO modifyAuthor (
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody AuthorReqDTO authorReqDTO
    ) {
        return authorService.update(id, authorReqDTO);
    }

    @DeleteMapping(value = "/api/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAuthor (
            @PathVariable(name = "id") Long id
    ) {
        authorService.deleteById(id);
    }

}
