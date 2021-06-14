package ru.otus.homework.parsers;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.service.MessageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.BDDMockito.given;

@DisplayName("Класс ParserCSV")
@ExtendWith(MockitoExtension.class)
public class ParserCSVTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ParserCSV parser;

    @DisplayName("создаст QuestionBook из полученного списка вопросов")
    @Test
    void getQuestionBookTest() {

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

    @DisplayName("выдаст ошибку при значении null во входном параметре")
    void getQuestionBookTest_QuestionsListIsNullError() {

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestionBook(null);
        });
    }

    @DisplayName("выдаст ошибку при пустом листе вопросов")
    void getQuestionBookTest_QuestionsListIsEmptyError() {

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestionBook(new ArrayList<>());
        });
    }

    @DisplayName("разбор строки с вопросом корректно разберёт все вопросы")
    @Test
    void getQuestionTest() {

        List<String> questions = getQuestions();
        for(String questionStr : questions) {
            Question question = parser.getQuestion(questionStr, ",");
            Assert.assertNotNull(question);
            Assert.assertNotEquals("", question.getText());
            Assert.assertNotNull(question.getType());
            assertThat(question.getType()).isIn(QuestionTypes.values());
            assertThat(question.getAnswers()).hasSizeGreaterThan(0);
        }

    }

    @DisplayName("разбор строки с вопросом корректно обработает пробелы в вопросе и типе вопроса")
    @Test
    void getQuestionTest_IgnoreSpacesInQuestionAndType() {

        Question question = parser.getQuestion("  Current capital of Russia?   ,  typing  ,Moscow:true", ",");
        Assert.assertEquals("Current capital of Russia?", question.getText());
        Assert.assertEquals(question.getType(), QuestionTypes.TYPING);
    }

    @DisplayName("разбор строки с вопросом получит один ответ на вопрос и тип ответа с вводом с клавиатуры")
    @Test
    void getQuestionTest_QuestionHasOneAnswer() {

        Question question = parser.getQuestion("Current capital of Russia?,typing,Moscow:true", ",");
        Assert.assertEquals("Current capital of Russia?", question.getText());
        Assert.assertEquals(question.getType(), QuestionTypes.TYPING);
        assertThat(question.getAnswers()).hasSize(1);
    }

    @DisplayName("разбор строки с вопросом получит три ответа на вопрос и тип одиночного ответа")
    @Test
    void getQuestionTest_QuestionHasThreeAnswers() {

        Question question = parser.getQuestion("JavaScript is language for?,single,Only for frontend:false,Only for backend:false,Frontend and backend:true", ",");
        Assert.assertEquals("JavaScript is language for?", question.getText());
        Assert.assertEquals(question.getType(), QuestionTypes.SINGLE);
        assertThat(question.getAnswers()).hasSize(3);
    }

    @DisplayName("разбор строки с вопросом получит четыре ответа на вопрос и тип множественного ответа")
    @Test
    void getQuestionTest_QuestionHasFourAnswers() {

        Question question = parser.getQuestion("Mark languages and frameworks for current course ...,multy,Java:true,Angular:false,Spring:true,TypeScript:false", ",");
        Assert.assertEquals("Mark languages and frameworks for current course ...", question.getText());
        Assert.assertEquals(question.getType(), QuestionTypes.MULTY);
        assertThat(question.getAnswers()).hasSize(4);
    }

    @DisplayName("разбор строки с вопросом выдаст ошибку при отсутствии строки с вопросом")
    @Test
    void getQuestionTest_settingIsNullError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestion(null, ",");
        });
    }

    @DisplayName("разбор строки с вопросом выдаст ошибку при отсутствии разделителя")
    @Test
    void getQuestionTest_separatorIsNullError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestion("Current capital of Russia?,typing,Moscow:true", null);
        });
    }

    @DisplayName("разбор строки с вопросом выдаст ошибку при отсутствии вопроса")
    @Test
    void getQuestionTest_settingIsEmptyError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestion("", ",");
        });
    }

    @DisplayName("разбор строки с вопросом выдаст ошибку при отсутствии непробельных символов в вопросе")
    @Test
    void getQuestionTest_settingIsEmptyIgnoreSpacesError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestion("   ", ",");
        });
    }

    @DisplayName("разбор строки с вопросом выдаст ошибку при отсутствии типа вопроса")
    @Test
    void getQuestionTest_questionTypeIsEmptyError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestion("Current capital of Russia?", ",");
        });
    }

    @DisplayName("разбор строки с вопросом выдаст ошибку при пустом вопросе")
    @Test
    void getQuestionTest_questionTextIsEmptyError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestion(",typing,Moscow:true", ",");
        });
    }

    @DisplayName("разбор строки с вопросом выдаст ошибку при несуществующем типе вопроса")
    @Test
    void getQuestionTest_questionTypeUnknownError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestion("Current capital of Russia?,unknownQuestionType,Moscow:true", ",");
        });
    }

    @DisplayName("разбор строки с вопросом выдаст ошибку при отсутствии ответов на вопрос")
    @Test
    void getQuestionTest_questionAnswersIsNullOrEmpty() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getQuestion("Current capital of Russia?,typing", ",");
        });
    }

    @DisplayName("разбор строки с ответом корректно разберёт все варианты ответов из всех вопросов")
    @Test
    void getAnswerTest() {

        List<String> questions = getQuestions();

        for(String question : questions) {
            String[] questionStrings = question.split(",");
            List<String> answers = Arrays.stream(questionStrings).skip(2).collect(Collectors.toList());
            for(String answer : answers) {
                Answer questionAnswer = parser.getAnswer(answer, ":");
                Assert.assertNotNull(questionAnswer);
                Assert.assertNotEquals("", questionAnswer.getText());
                Assert.assertNotNull(questionAnswer.getIsValid());
            }
        }
    }

    @DisplayName("разбор строки с ответом корректно обработает пробелы в ответе и признаке правильности")
    @Test
    void getAnswerTest_IgnoreSpacesInAnswerAndIsValid() {

        Answer questionAnswer = parser.getAnswer("  Moscow  :  true  ", ":");
        Assert.assertEquals("Moscow", questionAnswer.getText());
        Assert.assertTrue(questionAnswer.getIsValid());
    }

    @DisplayName("разбор строки с ответом признак правильности только true считает как правильный ответ")
    @Test
    void getAnswerTest_IsValidParseAsTrue() {

        Answer questionAnswer = parser.getAnswer("Moscow:true", ":");
        Assert.assertEquals("Moscow", questionAnswer.getText());
        Assert.assertTrue(questionAnswer.getIsValid());
    }

    @DisplayName("разбор строки с ответом пустой признак правильности считает как неправильный ответ")
    @Test
    void getAnswerTest_EmptyAnswerIsFalse() {

        Answer questionAnswer = parser.getAnswer("Moscow:", ":");
        Assert.assertEquals("Moscow", questionAnswer.getText());
        Assert.assertFalse(questionAnswer.getIsValid());
    }

    @DisplayName("разбор строки с ответом признак правильности false считает как неправильный ответ")
    @Test
    void getAnswerTest_IsValidParseAsFalse() {

        Answer questionAnswer = parser.getAnswer("Moscow:false", ":");
        Assert.assertEquals("Moscow", questionAnswer.getText());
        Assert.assertFalse(questionAnswer.getIsValid());
    }

    @DisplayName("разбор строки с ответом любой другой текст признака правильности считает как неправильный ответ")
    @Test
    void getAnswerTest_IsValidOtherTextAsFalse() {

        Answer questionAnswer = parser.getAnswer("Moscow:otherText", ":");
        Assert.assertEquals("Moscow", questionAnswer.getText());
        Assert.assertFalse(questionAnswer.getIsValid());
    }

    @DisplayName("разбор строки с ответом выдаст ошибку при отсутствии строки с вопросом")
    @Test
    void getAnswerTest_settingIsNullError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getAnswer(null, ":");
        });
    }

    @DisplayName("разбор строки с ответом выдаст ошибку при отсутствии строки с разделителем")
    @Test
    void getAnswerTest_separatorIsNullError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getAnswer("Moscow:true", null);
        });
    }

    @DisplayName("разбор строки с ответом выдаст ошибку при отсутствии ответа на вопрос")
    @Test
    void getAnswerTest_answerIsNullError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getAnswer(":true", ":");
        });
    }

    @DisplayName("разбор строки с ответом выдаст ошибку отсутствии непробельных символов в ответе")
    @Test
    void getAnswerTest_answerIsNullIgnoreSpacesError() {

        given(messageService.getLocalizedMessage(Mockito.any(String.class))).willReturn("");

        Assertions.assertThrows(BusinessException.class, () -> {
            parser.getAnswer("   :true", ":");
        });
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
