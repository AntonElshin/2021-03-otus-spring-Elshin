package ru.otus.homework.service;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;

public interface QuestionBookService {

    String prepareQuestion(Integer questionNumber, Question question);

    void printQuestionBook(Boolean addAnswersFlag);

    Boolean validateQuestionBook(QuestionBook questionBook);

    void performTesting(Boolean addAnswersFlag);

    void askLanguage(Boolean addAnswersFlag);

    void setValidAnswerMinCount(Integer validAnswerMinCount);

}
