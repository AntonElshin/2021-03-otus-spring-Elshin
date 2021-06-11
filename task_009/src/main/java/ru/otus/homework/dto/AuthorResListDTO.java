package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResListDTO {

    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

}
