package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.domain.BookComment;
import ru.otus.homework.dto.BookCommentDTO;

@Mapper(componentModel = "spring")
public interface BookCommentMapper {

    BookCommentMapper INSTANCE = Mappers.getMapper(BookCommentMapper.class);

    BookCommentDTO toDto(BookComment bookComment);
    BookComment fromDto(BookCommentDTO bookCommentDTO);

}
