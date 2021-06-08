package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.homework.domain.BookComment;
import ru.otus.homework.dto.BookCommentIdTextDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookCommentIdTextMapper {

    BookCommentIdTextMapper INSTANCE = Mappers.getMapper(BookCommentIdTextMapper.class);

    BookCommentIdTextDTO toDto(BookComment bookComment);
    List<BookCommentIdTextDTO> toListDto(List<BookComment> bookComments);

}
