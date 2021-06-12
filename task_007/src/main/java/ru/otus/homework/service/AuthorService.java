package ru.otus.homework.service;

import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;

import java.util.List;

public interface AuthorService {

    Author add(String lastName, String firstName, String middleName);

    void update(long id, String lastName, String firstName, String middleName);

    void getById(long id);

    void deleteById(long id);

    void findAll();

    void findByParams(String lastName, String firstName, String middleName);

    void count();

}
