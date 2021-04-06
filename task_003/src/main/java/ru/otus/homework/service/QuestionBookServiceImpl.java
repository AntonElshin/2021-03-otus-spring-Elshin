package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.loaders.Loader;
import ru.otus.homework.parsers.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
@Service
public class QuestionBookServiceImpl implements QuestionBookService {

    private final Loader loader;
    private final Parser parser;

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
    public void printQuestionBook(PrintStream out) throws BusinessException {

        List<String> questions = loader.loadQuestions(resourceName);
        QuestionBook questionBook = parser.getQuestionBook(questions);
        validateQuestionBook(questionBook);

        String questionStr = "\n";

        Integer questionNumber = 1;

        for(Question question : questionBook.getQuestions()) {

            questionStr += prepareQuestion(questionNumber, question);
            questionNumber++;
            questionStr += "\n\n";

        }

        try(PrintStream printStream = new PrintStream(out)) {
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

    @Override
    public void performTesting(InputStream in, PrintStream printStream, Boolean addAnswersFlag) throws BusinessException {

        Scanner scanner = new Scanner(in);
        List<String> questionAnswers = new ArrayList<>();
        String studentName = "";
        String studentSurname = "";

        List<String> questions = loader.loadQuestions(resourceName);
        QuestionBook questionBook = parser.getQuestionBook(questions);
        validateQuestionBook(questionBook);

        Integer questionNumber = 1;

        try(scanner; printStream) {

            byte[] nameStrBytes = ("\nPlease enter your name: ").getBytes();
            printStream.write(nameStrBytes);
            studentName = scanner.nextLine();
            if(addAnswersFlag) {
                printStream.write(studentName.getBytes());
            }

            byte[] surnameStrBytes = ("\nPlease enter your surname: ").getBytes();
            printStream.write(surnameStrBytes);
            studentSurname = scanner.nextLine();
            if(addAnswersFlag) {
                printStream.write(studentSurname.getBytes());
            }

            for(Question question : questionBook.getQuestions()) {

                String questionStr = "\n";

                if(questionNumber != 1) {
                    questionStr = "\n";
                }

                questionStr += prepareQuestion(questionNumber, question);
                questionNumber++;

                byte[] questionStrBytes = questionStr.getBytes();
                printStream.write(questionStrBytes);

                String questionAnswer = scanner.nextLine();
                if(addAnswersFlag) {
                    printStream.write(questionAnswer.getBytes());
                }
                questionAnswers.add(questionAnswer);

            }

            String resultStr = prepareTestResult(studentName, studentSurname, questionBook, questionAnswers);

            byte[] resultStrBytes = resultStr.getBytes();
            printStream.write(resultStrBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String prepareTestResult(
            String studentName,
            String studentSurname,
            QuestionBook questionBook,
            List<String> questionAnswers
    ) throws BusinessException {

        if(questionBook.getQuestions().size() != questionAnswers.size()) {
            throw new BusinessException(
                    Errors.ANSWER_SEPARATOR_IS_NULL,
                    questionAnswers.size(),
                    questionBook.getQuestions().size());
        }
        if(questionAnswers.size() == 0) {
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
        if(!studentName.trim().isEmpty() || !studentSurname.trim().isEmpty()) {
            resultStr.append("Dear, ");
            if(!studentName.trim().isEmpty() && studentSurname.trim().isEmpty()) {
                resultStr.append(studentName);
            }
            else if(studentName.trim().isEmpty() && !studentSurname.trim().isEmpty()) {
                resultStr.append(studentSurname);
            }
            else {
                resultStr.append(studentName + " " + studentSurname);
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

    private List<String> uniqueAnswersMultyTypeQuestion(String answer) {

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
}
