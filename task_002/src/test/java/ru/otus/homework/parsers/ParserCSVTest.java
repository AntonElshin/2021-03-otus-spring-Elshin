package ru.otus.homework.parsers;

import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.config.HomeworkApplicationConfig;
import org.junit.jupiter.api.Test;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.loaders.Loader;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Import(HomeworkApplicationConfig.class)
public class ParserCSVTest {

    @Autowired
    private Loader loader;

    @Autowired
    private Parser parser;

    @Value("${resource.name}")
    private String resourceName;

    @Test
    public void getQuestionBookTest() throws Exception {

        List<String> questions = loader.loadQuestions(resourceName);
        QuestionBook questionBook = parser.getQuestionBook(questions);

        Assert.assertEquals(5, questionBook.getQuestions().size());

        for(Question question : questionBook.getQuestions()) {
            Assert.assertNotNull(question.getText());
            Assert.assertNotNull(question.getType());

            Boolean minOneValidAnswerFlag = false;

            for(Answer answer : question.getAnswers()) {
                Assert.assertNotNull(answer.getText());
                if(answer.getIsValid() && !minOneValidAnswerFlag) {
                    minOneValidAnswerFlag = true;
                }
            }

            Assert.assertTrue(minOneValidAnswerFlag);
        }

    }

}
