package ru.otus.homework.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.library.domain.BookComment;
import ru.otus.homework.library.dto.BookCommentReqDTO;
import ru.otus.homework.library.dto.BookCommentResDTO;
import ru.otus.homework.library.dto.BookCommentResListDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookCommentMapper {

    BookCommentMapper INSTANCE = Mappers.getMapper(BookCommentMapper.class);

    BookComment fromDto(BookCommentReqDTO bookCommentReqDTO);

    BookCommentResDTO toDto(BookComment bookComment);
    List<BookCommentResListDTO> toListDto(List<BookComment> bookComments);

}
