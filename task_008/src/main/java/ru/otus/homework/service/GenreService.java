package ru.otus.homework.service;

import ru.otus.homework.domain.Genre;

public interface GenreService {

    Genre add(String name, String description);

    void update(long id, String name, String description);

    void getById(long id);

    void deleteById(long id);

    void findAll();

    void findByParams(String name);

    void count();

}
