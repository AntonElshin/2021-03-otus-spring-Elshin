package ru.otus.homework.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mockito;
import ru.otus.homework.dao.QuestionBookDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(locations = {"file:src/test/resources/spring-context.xml"})
public class QuestionBookServiceImplTest {

    @MockBean
    private QuestionBookDao questionBookDao;

    @Autowired
    private QuestionBookService questionBookService;

    QuestionBook questionBook = new QuestionBook();

    @BeforeEach
    public void setUp() {

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

    }

    @Test
    public void getQuestionBook() throws Exception {

        List<String> questionsFromFile = getQuestions();

        Mockito.when(questionBookDao.getQuestionBook(questionsFromFile)).thenReturn(questionBook);

        questionBookService.getQuestionBook(questionsFromFile);

        Mockito.verify(questionBookDao, Mockito.times(1))
                .getQuestionBook(questionsFromFile);

    }

    @Test
    public void validateQuestionBook () throws Exception {

        Assert.assertTrue(questionBookService.validateQuestionBook(questionBook));

    }


    @Test
    public void validateQuestionBook_Typing_TotalAnswerCount() throws Exception {

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

    private List<String> getQuestions() {

        List<String> questionsFromFile = new ArrayList<>();
        questionsFromFile.add("Current capital of Russia?,typing,Moscow:true");
        questionsFromFile.add("JavaScript is language for?,single,Only for frontend:false,Only for backend:false,Frontend and backend:true");
        questionsFromFile.add("Mark languages and frameworks for current course ...,multy,Java:true,Angular:false,Spring:true,TypeScript:false");
        questionsFromFile.add("Print name of company for which course customized for?,typing,Diasoft:true");
        questionsFromFile.add("Select name of education company?,single,University:false,Udemy:false,Otus:true");

        return questionsFromFile;

    }

}
