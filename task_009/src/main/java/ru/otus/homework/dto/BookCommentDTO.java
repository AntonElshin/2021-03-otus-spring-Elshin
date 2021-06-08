package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentDTO {

    private Long id;

    @NotNull(message="Комментарий должен быть привязан к книге")
    private BookIdDTO book;

    @NotNull(message="Необходимо указать текст комментария")
    @NotBlank(message="Текст комментария не может быть пустой строкой")
    private String text;

    public BookCommentDTO(BookIdDTO book, String text) {
        this.book = book;
        this.text = text;
    }
}
