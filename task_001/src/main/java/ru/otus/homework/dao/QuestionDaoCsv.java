package ru.otus.homework.dao;

import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDaoCsv implements QuestionDao {

    private AnswerDao answerDao;

    public QuestionDaoCsv(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Override
    public Question getQuestion(String setting, String separator) throws BusinessException {

        if(setting == null) {
            throw new BusinessException(Errors.QUESTION_SETTING_IS_NULL, setting);
        }
        if(separator == null) {
            throw new BusinessException(Errors.QUESTION_SEPARATOR_IS_NULL, separator);
        }

        String[] questionStrings = setting.split(separator);

        if(questionStrings.length > 2) {

            Question question = new Question();

            String text = (questionStrings[0]).trim();
            String type = (questionStrings[1]).trim();
            List<String> answers = Arrays.stream(questionStrings).skip(2).collect(Collectors.toList());

            question.setText(text);
            QuestionTypes questionType = QuestionTypes.getByName(type);

            if(questionType == null) {
                throw new BusinessException(Errors.QUESTION_TYPE_NOT_FOUND, questionType);
            }

            question.setType(questionType);

            List<Answer> questionAnswers = new java.util.ArrayList<>();

            for(String answer : answers) {
                Answer questionAnswer = answerDao.getAnswer(answer, ":");
                questionAnswers.add(questionAnswer);
            }

            if(questionAnswers.size() == 0) {
                throw new BusinessException(Errors.QUESTION_DOES_NOT_HAVE_ANSWER_OPTIONS);
            }

            question.setAnswers(questionAnswers);

            return question;

        }

        return null;
    }

}
