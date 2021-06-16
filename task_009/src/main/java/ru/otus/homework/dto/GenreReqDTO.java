package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreReqDTO {

    @NotNull(message="Необходимо указать название жанра")
    @NotBlank(message="Название жанра не может быть пустой строкой")
    private String name;

    private String description;

}
