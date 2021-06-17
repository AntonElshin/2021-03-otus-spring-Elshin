package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReqDTO {

    @NotNull(message="Необходимо указать название книги")
    @NotBlank(message="Название книги не может быть пустой строкой")
    private String title;

    private String isbn;

    private String description;

    @Size(min=1, message = "У книги должен быть хотя бы один автор")
    private List<AuthorReqIdDTO> authors;

    @Size(min=1, message = "У книги должен быть хотя бы один жанр")
    private List<GenreReqIdDTO> genres;

}
