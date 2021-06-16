package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.GenreReqDTO;
import ru.otus.homework.dto.GenreResDTO;
import ru.otus.homework.dto.GenreResListDTO;
import ru.otus.homework.service.GenreService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping(value = "/api/genres/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GenreResListDTO> getAllGenres() {
        return genreService.findAll();
    }

    @GetMapping(value = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GenreResListDTO> findGenresByParams(
            @RequestParam(value = "name", required = false) String name
    ) {
        return genreService.findByParams(name);
    }

    @GetMapping(value = "/api/genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenreResDTO getGenre (
            @PathVariable(name = "id") Long id
    ) {
        return genreService.getById(id);
    }

    @PostMapping(value = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenreResDTO createGenre (
            @Valid @RequestBody GenreReqDTO genreReqDTO
    ) {
        return genreService.add(genreReqDTO);
    }

    @PutMapping(path = "/api/genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenreResDTO modifyGenre (
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody GenreReqDTO genreReqDTO
    ) {
        return genreService.update(id, genreReqDTO);
    }

    @DeleteMapping(value = "/api/genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteGenre (
            @PathVariable(name = "id") Long id
    ) {
        genreService.deleteById(id);
    }

}
