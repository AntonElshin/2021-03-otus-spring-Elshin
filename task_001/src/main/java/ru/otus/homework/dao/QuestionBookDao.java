package ru.otus.homework.dao;

import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.exceptions.BusinessException;

public interface QuestionBookDao {

    public QuestionBook getQuestionBook(String[] questions) throws BusinessException;

}
