package ru.otus.homework.dao;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.ArrayList;
import java.util.List;

public class QuestionBookDaoCsv implements QuestionBookDao {

    private QuestionDao questionDao;

    private QuestionBookDaoCsv(QuestionDao questionDao) {
       this.questionDao = questionDao;
    }

    @Override
    public QuestionBook getQuestionBook(List<String> questions) throws BusinessException {

        if(questions == null || (questions != null && questions.size() == 0)) {
            throw new BusinessException(Errors.QUESTION_LIST_IS_NULL);
        }

        QuestionBook questionBook = new QuestionBook();
        List<Question> bookQuestions = new ArrayList<>();

        for(String question : questions) {
            Question bookQuestion = questionDao.getQuestion(question, ",");
            bookQuestions.add(bookQuestion);
        }

        if(bookQuestions.size() == 0) {
            throw new BusinessException(Errors.QUESTION_LIST_IS_EMPTY);
        }

        questionBook.setQuestions(bookQuestions);

        return questionBook;
    }

}
