package ru.otus.homework;

import org.springframework.context.annotation.*;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.service.QuestionBookService;

@ComponentScan
@PropertySource("/application.properties")
public class HomeworkApplication {

    public static void main(String[] args) throws BusinessException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HomeworkApplication.class);
        QuestionBookService questionBookService = context.getBean(QuestionBookService.class);

        questionBookService.performTesting(false);
    }

}
