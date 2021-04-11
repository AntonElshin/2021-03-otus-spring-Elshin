package ru.otus.homework.service;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.exceptions.BusinessException;

import java.io.InputStream;
import java.io.PrintStream;

public interface QuestionBookService {

    String prepareQuestion(Integer questionNumber, Question question);

    void printQuestionBook() throws BusinessException;

    Boolean validateQuestionBook(QuestionBook questionBook);

    void performTesting(Boolean addAnswersFlag) throws BusinessException;

}
