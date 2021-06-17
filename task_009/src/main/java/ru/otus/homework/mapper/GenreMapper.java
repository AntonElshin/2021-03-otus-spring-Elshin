package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.GenreReqDTO;
import ru.otus.homework.dto.GenreResDTO;
import ru.otus.homework.dto.GenreResListDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    Genre fromDto(GenreReqDTO genreReqDTO);

    GenreResDTO toDto(Genre genre);
    List<GenreResListDTO> toListDto(List<Genre> genres);

}
