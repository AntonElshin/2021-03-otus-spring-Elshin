package ru.otus.homework.controller;

//import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.service.GenreService;
import ru.otus.homework.library.dto.*;

import java.util.List;

@RestController
public class GenreController {

    private GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping(value = "/api/genres/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GenreResListDTO>> getAllGenres() {
        return ResponseEntity.ok(genreService.findAll());
    }

}
