package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.*;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.loaders.Loader;
import ru.otus.homework.parsers.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Service
public class QuestionBookServiceImpl implements QuestionBookService {

    private final Loader loader;
    private final Parser parser;
    private InputStream in;
    private PrintStream out;
    private Scanner scanner;

    @Value("${resource.name}")
    private String resourceName;

    @Value("${valid.answer.min.count}")
    private Integer validAnswerMinCount;

    @Override
    public String prepareQuestion(Integer questionNumber, Question question) {

        StringBuilder questionStr = new StringBuilder("");
        StringBuilder answerOptions = new StringBuilder("");

        if(question.getType() != QuestionTypes.TYPING) {

            Integer answerNumber = 1;

            for(Answer answer : question.getAnswers()) {
                answerOptions.append(answerNumber + "." + answer.getText());
                answerNumber++;
                if(answerNumber < question.getAnswers().size() + 1) {
                    answerOptions.append("   ");
                }
            }

        }

        questionStr.append("----------------------------------------------------" + "\n");
        questionStr.append("Hint: " + question.getType().getQuestionHint()  + "\n");
        questionStr.append("Question example: " + question.getType().getQuestionExample()  + "\n");
        questionStr.append("Answer example: " + question.getType().getAnswerExample() + "\n");
        questionStr.append("----------------------------------------------------" + "\n\n");
        questionStr.append(questionNumber + "." + question.getText() + "   " + answerOptions.toString()  + "\n");
        questionStr.append("Our answer: ");

        return questionStr.toString();
    }

    @Override
    public void printQuestionBook() throws BusinessException {

        QuestionBook questionBook = initQuestionBook();

        String questionStr = "\n";

        Integer questionNumber = 1;

        for(Question question : questionBook.getQuestions()) {

            questionStr += prepareQuestion(questionNumber, question);
            questionNumber++;
            questionStr += "\n\n";

        }

        configurateEnvironment();

        try {
            byte[] questionStrBytes = questionStr.getBytes();
            out.write(questionStrBytes);

            in.close();
            out.close();
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Boolean validateQuestionBook(QuestionBook questionBook) {

        if(questionBook.getQuestions() == null || (questionBook.getQuestions() != null && questionBook.getQuestions().size() == 0)) {
            return false;
        }

        for(Question question : questionBook.getQuestions()) {

            if(question.getText() == null || (question.getText() != null && question.getText().trim().isEmpty())) {
                return false;
            }

            if(question.getType() == null) {
                return false;
            }

            Integer totalAnswerCount = 0;
            Integer trueAnswerCount = 0;
            Integer falseAnswerCount = 0;

            for(Answer answer : question.getAnswers()) {

                if(answer.getText() == null || (answer.getText() != null && answer.getText().trim().isEmpty())) {
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

    @Override
    public void performTesting(Boolean addAnswersFlag) throws BusinessException {

        configurateEnvironment();
        List<String> questionAnswers;

        QuestionBook questionBook = initQuestionBook();

        try {

            Student student = askStudentInfo(addAnswersFlag);

            questionAnswers = askQuestions(questionBook, addAnswersFlag);

            String resultStr = prepareResult(student, questionBook, questionAnswers);

            byte[] resultStrBytes = resultStr.getBytes();
            out.write(resultStrBytes);

            in.close();
            out.close();
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Student askStudentInfo(Boolean addAnswersFlag) {

        String studentName = "";
        String studentSurname = "";

        if(out == null) {
            configurateEnvironment();
        }

        try {

            byte[] nameStrBytes = ("\nPlease enter your name: ").getBytes();
            out.write(nameStrBytes);
            studentName = scanner.nextLine();
            if(addAnswersFlag) {
                out.write(studentName.getBytes());
            }

            byte[] surnameStrBytes = ("\nPlease enter your surname: ").getBytes();
            out.write(surnameStrBytes);
            studentSurname = scanner.nextLine();
            if(addAnswersFlag) {
                out.write(studentSurname.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Student student = new Student(studentName, studentSurname);

        return student;
    }

    public List<String> askQuestions(QuestionBook questionBook, Boolean addAnswersFlag) {

        List<String> questionAnswers = new ArrayList<>();
        Integer questionNumber = 1;

        if(out == null) {
            configurateEnvironment();
        }

        try {
            for(Question question : questionBook.getQuestions()) {

                String questionStr = "\n";

                if(questionNumber != 1) {
                    questionStr = "\n";
                }

                questionStr += prepareQuestion(questionNumber, question);
                questionNumber++;

                byte[] questionStrBytes = questionStr.getBytes();
                out.write(questionStrBytes);

                String questionAnswer = scanner.nextLine();
                if(addAnswersFlag) {
                    out.write(questionAnswer.getBytes());
                }
                questionAnswers.add(questionAnswer);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questionAnswers;

    }

    public String prepareResult(
            Student student,
            QuestionBook questionBook,
            List<String> questionAnswers
    ) throws BusinessException {

        if(questionAnswers != null && questionBook.getQuestions().size() != questionAnswers.size()) {
            throw new BusinessException(
                    Errors.QUESTION_AND_ANSWER_QUANTITY_NOT_EQUALS,
                    questionAnswers.size(),
                    questionBook.getQuestions().size());
        }
        if(questionAnswers == null || (questionAnswers != null && questionAnswers.size() == 0)) {
            throw new BusinessException(Errors.ANSWER_LIST_IS_EMPTY);
        }

        Integer questionQuantity = questionBook.getQuestions().size();
        Integer totalValidAnswer = 0;

        for(int i=0; i<questionQuantity; i++) {

            Question curQuestion = questionBook.getQuestions().get(i);
            String curAnswer = questionAnswers.get(i);
            Boolean answerIsValid = false;

            if(curQuestion.getType() == QuestionTypes.MULTY) {

                List<String> uniqueAnswers = uniqueAnswersMultyTypeQuestion(curAnswer);

                Integer questionValidAnswer = 0;
                Integer givenValidAnswer = 0;
                Integer answerIndex = 1;

                for(Answer answer : curQuestion.getAnswers()) {
                    if(answer.getIsValid()) {
                        questionValidAnswer++;
                        for(String uniqueAnswer : uniqueAnswers) {
                            if(("" + answerIndex).equalsIgnoreCase(uniqueAnswer.trim())) {
                                givenValidAnswer++;
                            }
                        }
                    }
                    answerIndex++;
                }

                if(questionValidAnswer == givenValidAnswer && questionValidAnswer == uniqueAnswers.size()) {
                    answerIsValid = true;
                }

            }
            else if (curQuestion.getType() == QuestionTypes.SINGLE) {

                Integer answerIndex = 1;

                for(Answer answer : curQuestion.getAnswers()) {
                    if(("" + answerIndex).equalsIgnoreCase(curAnswer.trim()) && answer.getIsValid()) {
                        answerIsValid = true;
                        break;
                    }
                    answerIndex++;
                }
            }
            else {
                for(Answer answer : curQuestion.getAnswers()) {
                    if(answer.getText().trim().equalsIgnoreCase(curAnswer.trim()) && answer.getIsValid()) {
                        answerIsValid = true;
                        break;
                    }
                }
            }

            if(answerIsValid) {
                totalValidAnswer++;
            }

        }

        StringBuilder resultStr = new StringBuilder("\n");
        resultStr.append("----------------------------------------------------\n");

        if((student.getName() != null && !student.getName().trim().isEmpty())
            || (student.getSurname() != null && !student.getSurname().trim().isEmpty())) {
            resultStr.append("Dear, ");
            if(!student.getName().trim().isEmpty() && student.getSurname().trim().isEmpty()) {
                resultStr.append(student.getName());
            }
            else if(student.getName().trim().isEmpty() && !student.getSurname().trim().isEmpty()) {
                resultStr.append(student.getSurname());
            }
            else {
                resultStr.append(student.getName() + " " + student.getSurname());
            }
            resultStr.append("\n");
        }
        resultStr.append("Test result: " + totalValidAnswer + " valid answers of " + questionQuantity + " questions\n");
        if(totalValidAnswer >= validAnswerMinCount) {
            resultStr.append("Test passed successfully!");
        }
        else {
            resultStr.append("Test failed! Please try again ... ");
        }
        resultStr.append("\n");
        resultStr.append("----------------------------------------------------\n");

        return resultStr.toString();

    }

    public List<String> uniqueAnswersMultyTypeQuestion(String answer) {

        List<String> uniqueAnswers = new ArrayList<>();

        String[] answers = answer.trim().split(" ");

        for(int j=0; j<answers.length; j++) {
            String givenAnswer = answers[j];
            if(givenAnswer.trim().isEmpty()) {
                continue;
            }

            Boolean foundFlag = false;

            for(String uniqueAnswer : uniqueAnswers) {
                if(uniqueAnswer.trim().equalsIgnoreCase(givenAnswer)) {
                    foundFlag = true;
                    break;
                }
            }

            if(!foundFlag) {
                uniqueAnswers.add(givenAnswer);
            }
        }

        return uniqueAnswers;

    }

    public void setValidAnswerMinCount(Integer validAnswerMinCount) {
        this.validAnswerMinCount = validAnswerMinCount;
    }

    public Integer getValidAnswerMinCount() {
        return validAnswerMinCount;
    }

    public QuestionBook initQuestionBook() throws BusinessException {

        List<String> questions = loader.loadQuestions(resourceName);
        QuestionBook questionBook = parser.getQuestionBook(questions);
        validateQuestionBook(questionBook);

        return questionBook;

    }

    private Scanner configurateEnvironment() {

        in = System.in;
        out = System.out;
        scanner = new Scanner(in);

        return scanner;

    }
}
