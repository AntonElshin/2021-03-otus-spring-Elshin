package ru.otus.homework.service;

import ru.otus.homework.dto.GenreDTO;

import java.util.List;

public interface GenreService {

    GenreDTO add(GenreDTO genreDTO);

    GenreDTO update(long id, GenreDTO genreDTO);

    GenreDTO getById(long id);

    void deleteById(long id);

    List<GenreDTO> findAll();

    List<GenreDTO> findByParams(String name);

}
