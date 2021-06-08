package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWithCommentsDTO {

    private Long id;

    private String title;

    private String isbn;

    private String description;

    private List<AuthorDTO> authors;

    private List<GenreDTO> genres;

    private List<BookCommentIdTextDTO> comments;

}
