package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookReqDTO;
import ru.otus.homework.dto.BookResDTO;
import ru.otus.homework.dto.BookResListDTO;
import ru.otus.homework.dto.BookResWithCommentsDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book fromDto(BookReqDTO bookReqDTO);

    BookResDTO toDto(Book book);
    BookResWithCommentsDTO toWithCommentsDto(Book book);
    List<BookResListDTO> toListDto(List<Book> books);

}
