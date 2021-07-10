package ru.otus.homework.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.library.api.GenreApi;
import ru.otus.homework.library.dto.GenreReqDTO;
import ru.otus.homework.library.dto.GenreResDTO;
import ru.otus.homework.library.dto.GenreResListDTO;
import ru.otus.homework.library.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController implements GenreApi {

    private final GenreService genreService;

    @Override
    public ResponseEntity<List<GenreResListDTO>> getAllGenres() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @Override
    public ResponseEntity<List<GenreResListDTO>> findGenresByParams(String genreName) {
        return ResponseEntity.ok(genreService.findByParams(genreName));
    }

    @Override
    public ResponseEntity<GenreResDTO> getGenre(Long genreId) {
        return ResponseEntity.ok(genreService.getById(genreId));
    }

    @Override
    public ResponseEntity<GenreResDTO> createGenre(GenreReqDTO genreReqDTO) {
        return ResponseEntity.ok(genreService.add(genreReqDTO));
    }

    @Override
    public ResponseEntity<GenreResDTO> modifyGenre(Long genreId, GenreReqDTO genreReqDTO) {
        return ResponseEntity.ok(genreService.update(genreId, genreReqDTO));
    }

    @Override
    public ResponseEntity<Void> deleteGenre(Long genreId) {
        genreService.deleteById(genreId);
        return ResponseEntity.ok(null);
    }

}
