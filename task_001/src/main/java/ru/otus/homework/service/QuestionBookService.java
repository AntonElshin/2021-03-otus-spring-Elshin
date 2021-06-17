package ru.otus.homework.service;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.exceptions.BusinessException;

import java.util.List;

public interface QuestionBookService {

    QuestionBook getQuestionBook(List<String> questions) throws BusinessException;

    List<String> readQuestions();

    String prepareQuestion(Integer questionNumber, Question question);

    void printQuestionBook() throws BusinessException;

    Boolean validateQuestionBook(QuestionBook questionBook);

}
