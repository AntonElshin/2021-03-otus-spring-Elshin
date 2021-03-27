package ru.otus.homework.dao;

import ru.otus.homework.domain.Answer;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

public class AnswerDaoCsv implements AnswerDao {

    @Override
    public Answer getAnswer(String setting, String separator) throws BusinessException {

        if(setting == null) {
            throw new BusinessException(Errors.ANSWER_SETTING_IS_NULL, setting);
        }
        if(separator == null) {
            throw new BusinessException(Errors.ANSWER_SEPARATOR_IS_NULL, separator);
        }

        String[] answerStrings = setting.split(separator);

        String text = "";
        Boolean isValid = false;

        if(answerStrings.length > 1) {
            text = answerStrings[0] != null ? (answerStrings[0]).trim() : "";
            isValid = answerStrings[1] != null ? (answerStrings[1]).trim().equalsIgnoreCase("true") : false;
        }

        if(text.isEmpty()) {
            throw new BusinessException(Errors.ANSWER_TEXT_IS_EMPTY, text);
        }

        return new Answer(text, isValid);

    }
}
