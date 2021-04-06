package ru.otus.homework.service;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.loaders.Loader;
import ru.otus.homework.parsers.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс QuestionBookServiceImpl")
@ExtendWith(MockitoExtension.class)
public class QuestionBookServiceImplTest {

    @Mock
    private Loader loader;
    @Mock
    private Parser parser;

    @InjectMocks
    private QuestionBookServiceImpl questionBookService;

    private InputStream consoleInputStream = System.in;
    private PrintStream consoleOutputStream = System.out;

    @AfterEach
    void restoreDefault() {
        System.setIn(consoleInputStream);
        System.setOut(consoleOutputStream);
    }

    @DisplayName("Подготовит вопросы для вывода")
    @Test
    void prepareQuestionTest() throws Exception {

        QuestionBook questionBook = getQuestionBook();

        Integer questionNumber = 1;

        for(Question question : questionBook.getQuestions()) {

            String str = questionBookService.prepareQuestion(questionNumber, question);

            assertAll(str,
                    () -> str.contains(question.getText()),
                    () -> str.contains(question.getType().getName()),
                    () -> str.contains(question.getType().getQuestionExample()),
                    () -> str.contains(question.getType().getQuestionHint()),
                    () -> str.contains("Our answer: ")
            );

            questionNumber++;

        }

    }

    @DisplayName("Выдаст ошибку при валидации QuestionBook из-за двух ответов в вопросе с вводом единственного варианта")
    @Test
    void validateQuestionBook_Typing_TotalAnswerCountIncorrect() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Moscow", true));
        answers.add(new Answer("Yaroslavl", false));

        Question question = new Question();
        question.setText("Current capital of Russia?");
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("Выведет список вопросов")
    @Test
    public void printQuestionBookTest() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());

        try(ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setOut(out);

            questionBookService.printQuestionBook(out);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Current capital of Russia?"),
                    () -> str.contains("JavaScript is language for?"),
                    () -> str.contains("Mark languages and frameworks for current course ..."),
                    () -> str.contains("Print name of company for which course customized for?"),
                    () -> str.contains("Select name of education company?")
            );


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @DisplayName("Проведёт тест с правильными ответами на все вопросы")
    @Test
    public void performTestingTest_AllAnswersValid() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "Anton\nElshin\nMoscow\n3\n1 3\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Test result: 5 valid answers of 5 questions"),
                    () -> str.contains("Test passed successfully!")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("При ответе на вопросы теста игнорирует пробелы справа и слева от значимого текста")
    @Test
    public void performTestingTest_AnswerWithSpaceProcessedCorrect() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "Anton\nElshin\n   Moscow   \n 3 \n  1  3  \n  Diasoft  \n  3  ";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Test result: 5 valid answers of 5 questions"),
                    () -> str.contains("Test passed successfully!")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Корректно обрабатывает дубли ответов на вопрос, предполагающий несколько ответов")
    @Test
    public void performTestingTest_MultyAnswerWithSpaceAndDuplication() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "Anton\nElshin\nMoscow\n3\n1 3 3 1\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Test result: 5 valid answers of 5 questions"),
                    () -> str.contains("Test passed successfully!")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Корректно обрабатывает произвольный порядок ответов на вопрос с несколькими ответами")
    @Test
    public void performTestingTest_MultyInverseAnswer() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "Anton\nElshin\nMoscow\n3\n3 1\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Test result: 5 valid answers of 5 questions"),
                    () -> str.contains("Test passed successfully!")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Не засчитывает правильный ответ при указании ошибочного ответа вместе с правильными для множественного ответа")
    @Test
    public void performTestingTest_MultyAnswerIncorrectWithOneUnvalidOption() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "Anton\nElshin\nMoscow\n3\n1 3 4\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Test result: 4 valid answers of 5 questions"),
                    () -> str.contains("Test passed successfully!")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Не засчитывает правильный ответ при указании ошибочного ответа вместе с правильными для единичного ответа")
    @Test
    public void performTestingTest_SingleTwoAnswerWithOneValidIncorrect() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "Anton\nElshin\nMoscow\n1 3\n1 3\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Test result: 4 valid answers of 5 questions"),
                    () -> str.contains("Test passed successfully!")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Корректно обрабатывает ответы на вопросы с печатью правильного ответа без учёта регистра")
    @Test
    public void performTestingTest_ignoreCaseValid() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "Anton\nElshin\nmoscow\n3\n1 3\ndiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Test result: 5 valid answers of 5 questions"),
                    () -> str.contains("Test passed successfully!")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Не выведет имя и фамилию студента, если данные не указаны")
    @Test
    public void performTestingTest_Anonymous() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "\n\nMoscow\n3\n1 3\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            Assert.assertFalse(str.contains("Dear, "));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Не засчитает тест при указании недостаточного количества правильных ответов")
    @Test
    public void performTestingTest_TestFailed() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);

        String answers = "Anton\nElshin\nParis\n1 2\n1 3\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            assertAll(str,
                    () -> str.contains("Test result: 3 valid answers of 5 questions"),
                    () -> str.contains("Test failed! Please try again ... ")
            );

        } catch (IOException e) {
            e.printStackTrace();
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

    private QuestionBook getQuestionBook() {

        QuestionBook questionBook = new QuestionBook();

        ArrayList<Answer> q1_answers = new ArrayList<>();
        q1_answers.add(new Answer("Moscow", true));

        ArrayList<Answer> q2_answers = new ArrayList<>();
        q2_answers.add(new Answer("Only for frontend", false));
        q2_answers.add(new Answer("Only for backend", false));
        q2_answers.add(new Answer("Frontend and backend", true));

        ArrayList<Answer> q3_answers = new ArrayList<>();
        q3_answers.add(new Answer("Java", true));
        q3_answers.add(new Answer("Angular", false));
        q3_answers.add(new Answer("Spring", true));
        q3_answers.add(new Answer("TypeScript", false));

        ArrayList<Answer> q4_answers = new ArrayList<>();
        q4_answers.add(new Answer("Diasoft", true));

        ArrayList<Answer> q5_answers = new ArrayList<>();
        q5_answers.add(new Answer("University", false));
        q5_answers.add(new Answer("Udemy", false));
        q5_answers.add(new Answer("Otus", true));

        Question question_1 = new Question();
        question_1.setText("Current capital of Russia?");
        question_1.setType(QuestionTypes.TYPING);
        question_1.setAnswers(q1_answers);

        Question question_2 = new Question();
        question_2.setText("JavaScript is language for?");
        question_2.setType(QuestionTypes.SINGLE);
        question_2.setAnswers(q2_answers);

        Question question_3 = new Question();
        question_3.setText("Mark languages and frameworks for current course ...");
        question_3.setType(QuestionTypes.MULTY);
        question_3.setAnswers(q3_answers);

        Question question_4 = new Question();
        question_4.setText("Print name of company for which course customized for?");
        question_4.setType(QuestionTypes.TYPING);
        question_4.setAnswers(q4_answers);

        Question question_5 = new Question();
        question_5.setText("Select name of education company?");
        question_5.setType(QuestionTypes.SINGLE);
        question_5.setAnswers(q5_answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question_1);
        questions.add(question_2);
        questions.add(question_3);
        questions.add(question_4);
        questions.add(question_5);

        questionBook.setQuestions(questions);

        return questionBook;

    }

}
