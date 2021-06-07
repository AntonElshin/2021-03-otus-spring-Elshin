package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDTO {

    private Long id;

    @NotNull(message="Необходимо указать название жанра")
    @NotBlank(message="Название жанра не может быть пустой строкой")
    private String name;

    private String description;

    public GenreDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
