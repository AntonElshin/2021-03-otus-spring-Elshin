package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.GenreDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDTO toDto(Genre genre);
    Genre fromDto(GenreDTO genreDTO);
    List<GenreDTO> toListDto(List<Genre> genres);

}
