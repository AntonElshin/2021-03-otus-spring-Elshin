package ru.otus.homework.service;

import ru.otus.homework.domain.Genre;

import java.util.List;

public interface PrintGenreService {

    String printGenre(Genre genre);

    String printGenres(List<Genre> genres);

    String printGenresCount(Long count);

    String prepareGenre(Genre genre);

}
