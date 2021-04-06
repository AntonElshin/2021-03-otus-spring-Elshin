package ru.otus.homework.parsers;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.loaders.Loader;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс ParserCSV")
@ExtendWith(MockitoExtension.class)
public class ParserCSVTest {

    @Mock
    private Loader loader;
    @InjectMocks
    private ParserCSV parser;

    @DisplayName("создаст QuestionBook из полученного списка вопросов")
    @Test
    void getQuestionBookTest() throws Exception {

        List<String> questions = getQuestions();

        QuestionBook questionBook = parser.getQuestionBook(questions);

        assertThat(questions).hasSize(5);

        for(Question question : questionBook.getQuestions()) {

            String questionText = question.getText();
            QuestionTypes questionType = question.getType();

            Assert.assertNotNull(questionText);
            assertThat(questionText).isNotBlank();
            Assert.assertNotNull(questionType);
            assertThat(questionType.getName()).isNotBlank();

            Boolean minOneValidAnswerFlag = false;

            for(Answer answer : question.getAnswers()) {
                String answerText = answer.getText();
                Boolean answerIsValid = answer.getIsValid();

                Assert.assertNotNull(answerText);
                assertThat(answerText).isNotBlank();
                Assert.assertNotNull(answerIsValid);

                if(answerIsValid && !minOneValidAnswerFlag) {
                    minOneValidAnswerFlag = true;
                }
            }

            Assert.assertTrue(minOneValidAnswerFlag);
        }

    }

    private List<String> getQuestions() {

        List<String> questions = new ArrayList<>();

        questions.add("Current capital of Russia?,typing,Moscow:true");
        questions.add("JavaScript is language for?,single,Only for frontend:false,Only for backend:false,Frontend and backend:true");
        questions.add("Mark languages and frameworks for current course ...,multy,Java:true,Angular:false,Spring:true,TypeScript:false");
        questions.add("Print name of company for which course customized for?,typing,Diasoft:true");
        questions.add("Select name of education company?,single,University:false,Udemy:false,Otus:true");

        return questions;

    }

}
