package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookWithCommentsDTO;

@Mapper(componentModel = "spring")
public interface BookWithCommentsMapper {

    BookWithCommentsMapper INSTANCE = Mappers.getMapper(BookWithCommentsMapper.class);

    BookWithCommentsDTO toDto(Book book);

}
