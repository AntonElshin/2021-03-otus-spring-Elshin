package ru.otus.homework.parsers;

import ru.otus.homework.domain.QuestionBook;

import java.util.List;

public interface Parser {

    QuestionBook getQuestionBook(List<String> questions);

}
