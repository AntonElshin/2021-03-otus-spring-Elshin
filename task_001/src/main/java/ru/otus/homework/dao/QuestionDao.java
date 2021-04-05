package ru.otus.homework.dao;

import ru.otus.homework.domain.Question;
import ru.otus.homework.exceptions.BusinessException;

public interface QuestionDao {

    public Question getQuestion(String setting, String separator) throws BusinessException;

}
