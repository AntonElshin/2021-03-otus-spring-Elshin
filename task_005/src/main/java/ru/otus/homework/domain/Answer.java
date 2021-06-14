package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Answer {

    private String text;
    private Boolean isValid;

}
