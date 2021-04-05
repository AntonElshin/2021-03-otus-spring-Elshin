package ru.otus.homework.service;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.exceptions.BusinessException;

public interface QuestionBookService {

    QuestionBook getQuestionBook(String[] questions) throws BusinessException;

    String[] readQuestions();

    String prepareQuestion(Integer questionNumber, Question question);

    void printQuestionBook(QuestionBook questionBook);

    Boolean validateQuestionBook(QuestionBook questionBook);

}
