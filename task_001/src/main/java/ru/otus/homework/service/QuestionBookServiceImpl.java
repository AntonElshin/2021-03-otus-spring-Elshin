package ru.otus.homework.service;

import ru.otus.homework.dao.QuestionBookDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.exceptions.BusinessException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class QuestionBookServiceImpl implements QuestionBookService {

    private QuestionBookDao questionBookDao;
    private String resourceName;

    private QuestionBookServiceImpl(QuestionBookDao questionBookDao, String resourceName) {
        this.questionBookDao = questionBookDao;
        this.resourceName = resourceName;
    }

    public QuestionBook getQuestionBook(String[] questions) throws BusinessException {
        QuestionBook questionBook = questionBookDao.getQuestionBook(questions);
        return questionBook;
    }

    @Override
    public String[] readQuestions() {

        List<String> questionsFromFile = new ArrayList<>();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        Scanner s = new Scanner(inputStream).useDelimiter(Pattern.compile("\\r\\n|\\r|\\n"));

        while(s.hasNext()) {
            String question = s.hasNext() ? s.next() : "";
            questionsFromFile.add(question);
        }

        return questionsFromFile.toArray(new String[0]);
    }

    @Override
    public String prepareQuestion(Integer questionNumber, Question question) {

        String questionStr = "";

        String answerOptions = "";

        if(question.getType() != QuestionTypes.TYPING) {

            Integer answerNumber = 1;

            for(Answer answer : question.getAnswers()) {
                answerOptions += answerNumber + ". " + answer.getText();
                answerNumber++;
                if(answerNumber < question.getAnswers().size() + 1) {
                    answerOptions += " ";
                }
            }

        }

        questionStr += "----------------------------------------------------" + "\n";
        questionStr += "Hint: " + question.getType().getQuestionHint()  + "\n";
        questionStr += "Question example: " + question.getType().getQuestionExample()  + "\n";
        questionStr += "Answer example: " + question.getType().getAnswerExample()  + "\n";
        questionStr += "----------------------------------------------------"  + "\n";
        questionStr += "\n";
        questionStr += questionNumber + ". " + question.getText() + " " + answerOptions  + "\n";
        questionStr += "Our answer: ";

        return questionStr;
    }

    @Override
    public void printQuestionBook(QuestionBook questionBook) {

        String questionStr = "";

        Integer questionNumber = 1;

        for(Question question : questionBook.getQuestions()) {

            questionStr += prepareQuestion(questionNumber, question);
            questionNumber++;
            questionStr += "\n\n";

        }

        try(PrintStream printStream = new PrintStream(System.out)) {
            byte[] questionStrBytes = questionStr.getBytes();
            printStream.write(questionStrBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Boolean validateQuestionBook(QuestionBook questionBook) {

        if(questionBook.getQuestions() == null && questionBook.getQuestions().size() == 0) {
            return false;
        }

        for(Question question : questionBook.getQuestions()) {

            if(question.getText() == null || question.getText().isEmpty()) {
                return false;
            }

            if(question.getType() == null) {
                return false;
            }

            Integer totalAnswerCount = 0;
            Integer trueAnswerCount = 0;
            Integer falseAnswerCount = 0;

            for(Answer answer : question.getAnswers()) {

                if(answer.getText() == null || answer.getText().isEmpty()) {
                    return false;
                }

                if(answer.getIsValid() == null) {
                    return false;
                }

                totalAnswerCount++;
                if(answer.getIsValid()) {
                    trueAnswerCount++;
                }
                else {
                    falseAnswerCount++;
                }

            }

            if(question.getType().getName() == "typing") {
                if(!(totalAnswerCount == 1 && trueAnswerCount == 1 && falseAnswerCount == 0)) {
                    return false;
                }
            }
            else if(question.getType().getName() == "single") {
                if(!(totalAnswerCount > 1 && trueAnswerCount == 1 && falseAnswerCount >= 1)) {
                    return false;
                }
            }
            else if(question.getType().getName() == "multy") {
                if(!(totalAnswerCount > 2 && trueAnswerCount >= 2 && falseAnswerCount >= 1)) {
                    return false;
                }
            }

        }

        return true;
    }

}
