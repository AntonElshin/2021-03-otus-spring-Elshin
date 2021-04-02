package ru.otus.homework.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import ru.otus.homework.loaders.Loader;
import ru.otus.homework.loaders.LoaderCSV;
import ru.otus.homework.parsers.Parser;
import ru.otus.homework.parsers.ParserCSV;
import ru.otus.homework.service.QuestionBookService;
import ru.otus.homework.service.QuestionBookServiceImpl;

@TestConfiguration
@PropertySource("/application-test.properties")
public class HomeworkApplicationConfig {

    @Bean
    public Loader loader() {
        return new LoaderCSV();
    }

    @Bean
    public Parser parser() {
        return new ParserCSV();
    }

    @Bean
    public QuestionBookService questionBookService(Loader loader, Parser parser) {
        return new QuestionBookServiceImpl(loader, parser);
    }

}
