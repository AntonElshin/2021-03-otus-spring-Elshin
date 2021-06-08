package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.domain.Author;
import ru.otus.homework.dto.AuthorDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO toDto(Author author);
    Author fromDto(AuthorDTO authorDTO);
    List<AuthorDTO> toListDto(List<Author> authors);

}
