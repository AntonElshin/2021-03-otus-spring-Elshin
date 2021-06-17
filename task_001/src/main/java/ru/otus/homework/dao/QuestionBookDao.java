package ru.otus.homework.dao;

import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.exceptions.BusinessException;

import java.util.List;

public interface QuestionBookDao {

    public QuestionBook getQuestionBook(List<String> questions) throws BusinessException;

}
