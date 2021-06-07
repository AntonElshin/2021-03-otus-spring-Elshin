package ru.otus.homework.service;

import ru.otus.homework.dto.AuthorDTO;

import java.util.List;

public interface AuthorService {

    AuthorDTO add(AuthorDTO authorDTO);

    AuthorDTO update(long id, AuthorDTO authorDTO);

    AuthorDTO getById(long id);

    void deleteById(long id);

    List<AuthorDTO> findAll();

    List<AuthorDTO> findByParams(String lastName, String firstName, String middleName);

}
