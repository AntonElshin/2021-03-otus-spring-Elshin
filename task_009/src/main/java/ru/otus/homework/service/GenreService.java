package ru.otus.homework.service;

import ru.otus.homework.dto.GenreReqDTO;
import ru.otus.homework.dto.GenreResDTO;
import ru.otus.homework.dto.GenreResListDTO;

import java.util.List;

public interface GenreService {

    GenreResDTO add(GenreReqDTO genreReqDTO);

    GenreResDTO update(long id, GenreReqDTO genreReqDTO);

    GenreResDTO getById(long id);

    void deleteById(long id);

    List<GenreResListDTO> findAll();

    List<GenreResListDTO> findByParams(String name);

}
