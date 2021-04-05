package ru.otus.homework.dao;

import ru.otus.homework.domain.Answer;
import ru.otus.homework.exceptions.BusinessException;

public interface AnswerDao {

    public Answer getAnswer(String setting, String separator) throws BusinessException;

}
