package ru.otus.homework.service;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.config.HomeworkApplicationConfig;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.loaders.Loader;
import ru.otus.homework.parsers.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@Import(HomeworkApplicationConfig.class)
public class QuestionBookServiceImplTest {

    @Autowired
    private Loader loader;

    @Autowired
    private Parser parser;

    @Autowired
    private QuestionBookService questionBookService;

    private InputStream consoleInputStream = System.in;
    private PrintStream consoleOutputStream = System.out;

    @AfterEach
    public void restoreDefault() {
        System.setIn(consoleInputStream);
        System.setOut(consoleOutputStream);
    }

    @Test
    public void validateQuestionBook_Typing_TotalAnswerCountIncorrect() throws Exception {

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

    @Test
    public void printQuestionBookTest() throws Exception {

        try(ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setOut(out);

            questionBookService.printQuestionBook(out);

            String str = outByte.toString();

            Assert.assertTrue(str.contains("Current capital of Russia?"));
            Assert.assertTrue(str.contains("JavaScript is language for?"));
            Assert.assertTrue(str.contains("Mark languages and frameworks for current course ..."));
            Assert.assertTrue(str.contains("Print name of company for which course customized for?"));
            Assert.assertTrue(str.contains("Select name of education company?"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void performTestingTest_AllAnswersValid() throws Exception {

        String answers = "Anton\nElshin\nMoscow\n3\n1 3\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            Assert.assertTrue(str.contains("Test result: 5 valid answers of 5 questions"));
            Assert.assertTrue(str.contains("Test passed successfully!"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void performTestingTest_MultyAnswerWithSpaceAndDuplication() throws Exception {

        String answers = "Anton\nElshin\n   Moscow   \n3\n1  3   3   1\n  Diasoft  \n  3  ";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            Assert.assertTrue(str.contains("Test result: 5 valid answers of 5 questions"));
            Assert.assertTrue(str.contains("Test passed successfully!"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void performTestingTest_MultyInverseAnswer() throws Exception {

        String answers = "Anton\nElshin\nMoscow\n3\n3 1\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            Assert.assertTrue(str.contains("Test result: 5 valid answers of 5 questions"));
            Assert.assertTrue(str.contains("Test passed successfully!"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void performTestingTest_MultyInverseAnswerIncorrect() throws Exception {

        String answers = "Anton\nElshin\nMoscow\n3\n1 3 4\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            Assert.assertTrue(str.contains("Test result: 4 valid answers of 5 questions"));
            Assert.assertTrue(str.contains("Test passed successfully!"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void performTestingTest_SingleTwoAnswerWithOneValidIncorrect() throws Exception {

        String answers = "Anton\nElshin\nMoscow\n1 3\n1 3 4\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            Assert.assertTrue(str.contains("Test result: 3 valid answers of 5 questions"));
            Assert.assertTrue(str.contains("Test failed! Please try again ... "));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void performTestingTest_ignoreCaseValid() throws Exception {

        String answers = "Anton\nElshin\nmoscow\n3\n1 3\ndiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            Assert.assertTrue(str.contains("Test result: 5 valid answers of 5 questions"));
            Assert.assertTrue(str.contains("Test passed successfully!"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void performTestingTest_Anonymous() throws Exception {

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

    @Test
    public void performTestingTest_TestFailed() throws Exception {

        String answers = "Anton\nElshin\nParis\n1 2\n1 3\nDiasoft\n3";

        try(InputStream in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outByte = new java.io.ByteArrayOutputStream();
            PrintStream out = new java.io.PrintStream(outByte);
        ) {
            System.setIn(in);
            System.setOut(out);

            questionBookService.performTesting(in, out, true);

            String str = outByte.toString();

            Assert.assertTrue(str.contains("Test result: 3 valid answers of 5 questions"));
            Assert.assertTrue(str.contains("Test failed! Please try again ... "));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
