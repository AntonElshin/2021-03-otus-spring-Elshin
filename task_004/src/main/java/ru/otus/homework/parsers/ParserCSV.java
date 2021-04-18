package ru.otus.homework.parsers;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParserCSV implements Parser {

    @Override
    public QuestionBook getQuestionBook(List<String> questions) throws BusinessException {

        if(questions == null || questions.size() == 0) {
            throw new BusinessException(Errors.QUESTION_LIST_IS_NULL);
        }

        QuestionBook questionBook = new QuestionBook();
        List<Question> bookQuestions = new ArrayList<>();

        for(String question : questions) {
            Question bookQuestion = getQuestion(question, ",");
            bookQuestions.add(bookQuestion);
        }

        if(bookQuestions.size() == 0) {
            throw new BusinessException(Errors.QUESTION_LIST_IS_EMPTY);
        }

        questionBook.setQuestions(bookQuestions);

        return questionBook;

    }

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

            if(text.isEmpty()) {
                throw new BusinessException(Errors.QUESTION_TEXT_IS_EMPTY);
            }

            QuestionTypes questionType = QuestionTypes.getByName(type);

            if(questionType == null) {
                throw new BusinessException(Errors.QUESTION_TYPE_NOT_FOUND, questionType);
            }

            question.setType(questionType);

            List<Answer> questionAnswers = new java.util.ArrayList<>();

            for(String answer : answers) {
                Answer questionAnswer = getAnswer(answer, ":");
                questionAnswers.add(questionAnswer);
            }

            if(questionAnswers.size() == 0) {
                throw new BusinessException(Errors.QUESTION_DOES_NOT_HAVE_ANSWER_OPTIONS);
            }

            question.setAnswers(questionAnswers);

            return question;

        }
        else if (questionStrings.length == 2) {
            throw new BusinessException(Errors.QUESTION_DOES_NOT_HAVE_ANSWER_OPTIONS);
        }
        else if(questionStrings.length == 1) {
            throw new BusinessException(Errors.QUESTION_TYPE_IS_EMPTY);
        }
        else {
            throw new BusinessException(Errors.QUESTION_TEXT_IS_EMPTY);
        }
    }

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

        if(answerStrings.length >= 1) {
            text = answerStrings[0] != null ? (answerStrings[0]).trim() : "";
            isValid = (answerStrings.length > 1 && answerStrings[1] != null) ? (answerStrings[1]).trim().equalsIgnoreCase("true") : false;
        }

        if(text.isEmpty()) {
            throw new BusinessException(Errors.ANSWER_TEXT_IS_EMPTY, text);
        }

        return new Answer(text, isValid);

    }
}
