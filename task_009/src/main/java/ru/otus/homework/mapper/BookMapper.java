package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDTO;
import ru.otus.homework.dto.BookResDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDTO toDto(Book book);
    Book fromDto(BookDTO bookDTO);
    List<BookResDTO> toListDto(List<Book> books);

}
