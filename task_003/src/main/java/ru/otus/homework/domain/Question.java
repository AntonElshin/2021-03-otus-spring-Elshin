package ru.otus.homework.domain;

import lombok.Data;

import java.util.List;

@Data
public class Question {

    private String text;
    private QuestionTypes type;
    private List<Answer> answers;

}
