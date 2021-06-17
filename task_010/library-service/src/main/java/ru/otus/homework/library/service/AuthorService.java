package ru.otus.homework.library.service;

import ru.otus.homework.library.dto.AuthorReqDTO;
import ru.otus.homework.library.dto.AuthorResDTO;
import ru.otus.homework.library.dto.AuthorResListDTO;

import java.util.List;

public interface AuthorService {

    AuthorResDTO add(AuthorReqDTO authorReqDTO);

    AuthorResDTO update(long id, AuthorReqDTO authorReqDTO);

    AuthorResDTO getById(long id);

    void deleteById(long id);

    List<AuthorResListDTO> findAll();

    List<AuthorResListDTO> findByParams(String lastName, String firstName, String middleName);

}
