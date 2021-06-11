package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorReqDTO {

    @NotNull(message="Необходимо указать фамилию автора")
    @NotBlank(message="Фамилия автора не может быть пустой строкой")
    private String lastName;

    @NotNull(message="Необходимо указать имя автора")
    @NotBlank(message="Имя автора не может быть пустой строкой")
    private String firstName;

    private String middleName;

}
