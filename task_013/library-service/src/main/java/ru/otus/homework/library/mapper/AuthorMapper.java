package ru.otus.homework.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.library.domain.Author;
import ru.otus.homework.library.dto.AuthorReqDTO;
import ru.otus.homework.library.dto.AuthorResDTO;
import ru.otus.homework.library.dto.AuthorResListDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    Author fromDto(AuthorReqDTO authorReqDTO);

    AuthorResDTO toDto(Author author);
    List<AuthorResListDTO> toListDto(List<Author> authors);

}
