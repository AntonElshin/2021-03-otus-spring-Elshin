package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.GenreDTO;
import ru.otus.homework.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping(value = "/api/genres/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GenreDTO> getAllGenres() {
        return genreService.findAll();
    }

    @GetMapping(value = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GenreDTO> findGenresByParams(
            @RequestParam(value = "name", required = false) String name
    ) {
        return genreService.findByParams(name);
    }

    @GetMapping(value = "/api/genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenreDTO getGenre (
            @PathVariable(name = "id") Long id
    ) {
        return genreService.getById(id);
    }

    @PostMapping(value = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenreDTO createGenre (
            @RequestBody GenreDTO genreDTO
    ) {
        return genreService.add(genreDTO);
    }

    @PutMapping(path = "/api/genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenreDTO modifyGenre (
            @PathVariable(name = "id") Long id,
            @RequestBody GenreDTO genreDTO
    ) {
        return genreService.update(id, genreDTO);
    }

    @DeleteMapping(value = "/api/genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteGenre (
            @PathVariable(name = "id") Long id
    ) {
        genreService.deleteById(id);
    }

}
