package ru.otus.homework;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.service.QuestionBookService;

public class HomeworkApplication {

    public static void main(String[] args) throws BusinessException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        QuestionBookService questionBookService = context.getBean(QuestionBookService.class);
        String[] questionsFromFile = questionBookService.readQuestions();

        QuestionBook questionBook = questionBookService.getQuestionBook(questionsFromFile);
        if(questionBookService.validateQuestionBook(questionBook)) {
            questionBookService.printQuestionBook(questionBook);
        }

    }

}
