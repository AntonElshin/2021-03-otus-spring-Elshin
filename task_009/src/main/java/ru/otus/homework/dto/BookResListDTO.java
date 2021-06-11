package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResListDTO {

    private Long id;

    private String title;

    private String isbn;

    private List<AuthorResListDTO> authors;

    private List<GenreResListDTO> genres;

}
