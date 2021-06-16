package ru.otus.homework.service;

import ru.otus.homework.dto.AuthorReqDTO;
import ru.otus.homework.dto.AuthorResDTO;
import ru.otus.homework.dto.AuthorResListDTO;

import java.util.List;

public interface AuthorService {

    AuthorResDTO add(AuthorReqDTO authorReqDTO);

    AuthorResDTO update(long id, AuthorReqDTO authorReqDTO);

    AuthorResDTO getById(long id);

    void deleteById(long id);

    List<AuthorResListDTO> findAll();

    List<AuthorResListDTO> findByParams(String lastName, String firstName, String middleName);

}
