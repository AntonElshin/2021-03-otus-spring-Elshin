package ru.otus.homework.parsers;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.exceptions.BusinessException;

import java.util.List;

public interface Parser {

    QuestionBook getQuestionBook(List<String> questions) throws BusinessException;

}
