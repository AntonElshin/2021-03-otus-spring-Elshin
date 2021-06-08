package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreIdDTO {

    @NotNull(message="Необходимо указать идентификатор жанра")
    private Long id;

}
