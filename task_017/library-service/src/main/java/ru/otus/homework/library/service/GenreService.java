package ru.otus.homework.library.service;

import ru.otus.homework.library.dto.GenreReqDTO;
import ru.otus.homework.library.dto.GenreResDTO;
import ru.otus.homework.library.dto.GenreResListDTO;

import java.util.List;

public interface GenreService {

    GenreResDTO add(GenreReqDTO genreReqDTO);

    GenreResDTO update(long id, GenreReqDTO genreReqDTO);

    GenreResDTO getById(long id);

    void deleteById(long id);

    List<GenreResListDTO> findAll();

    List<GenreResListDTO> findByParams(String name);

}
