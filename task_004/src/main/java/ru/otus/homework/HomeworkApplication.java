package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.service.QuestionBookService;

@SpringBootApplication
public class HomeworkApplication {

    public static void main(String[] args) throws BusinessException {

        ApplicationContext ctx = SpringApplication.run(HomeworkApplication.class, args);
        QuestionBookService questionBookService = ctx.getBean(QuestionBookService.class);
        questionBookService.performTesting(false);
    }

}
