package ru.otus.homework.parsers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.QuestionBook;
import ru.otus.homework.domain.QuestionTypes;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.service.MessageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParserCSV implements Parser {

    private final MessageService messageService;

    @Override
    public QuestionBook getQuestionBook(List<String> questions) {

        if(questions == null || questions.size() == 0) {
            throw new BusinessException(Errors.QUESTION_LIST_IS_NULL, messageService.getLocalizedMessage("exception.question_list_is_null"));
        }

        QuestionBook questionBook = new QuestionBook();
        List<Question> bookQuestions = new ArrayList<>();

        for(String question : questions) {
            Question bookQuestion = getQuestion(question, ",");
            bookQuestions.add(bookQuestion);
        }

        if(bookQuestions.size() == 0) {
            throw new BusinessException(Errors.QUESTION_LIST_IS_EMPTY, messageService.getLocalizedMessage("exception.question_list_is_empty"));
        }

        questionBook.setQuestions(bookQuestions);

        return questionBook;

    }

    public Question getQuestion(String setting, String separator) {
        if(setting == null) {
            String message = String.format("%s \"%s?,typing|single|multy,%s:true\", %s \"%s?,typing,%s:true)\", %s: null",
                    messageService.getLocalizedMessage("exception.question_setting_is_null"),
                    messageService.getLocalizedMessage("exception.question"),
                    messageService.getLocalizedMessage("exception.answer"),
                    messageService.getLocalizedMessage("exception.example"),
                    messageService.getLocalizedMessage("exception.question"),
                    messageService.getLocalizedMessage("exception.answer"),
                    messageService.getLocalizedMessage("exception.current_value")
            );
            throw new BusinessException(Errors.QUESTION_SETTING_IS_NULL, message);
        }
        if(separator == null) {
            throw new BusinessException(Errors.QUESTION_SEPARATOR_IS_NULL, messageService.getLocalizedMessage("exception.question_separator_is_null"));
        }

        String[] questionStrings = setting.split(separator);

        if(questionStrings.length > 2) {

            Question question = new Question();

            String text = (questionStrings[0]).trim();
            String type = (questionStrings[1]).trim();
            List<String> answers = Arrays.stream(questionStrings).skip(2).collect(Collectors.toList());

            question.setText(text);

            if(text.isEmpty()) {
                throw new BusinessException(Errors.QUESTION_TEXT_IS_EMPTY, messageService.getLocalizedMessage("exception.question_text_is_empty"));
            }

            QuestionTypes questionType = QuestionTypes.getByName(type);

            if(questionType == null) {
                throw new BusinessException(Errors.QUESTION_TYPE_NOT_FOUND, messageService.getLocalizedMessage("exception.question_type_not_found"));
            }

            question.setType(questionType);

            List<Answer> questionAnswers = new java.util.ArrayList<>();

            for(String answer : answers) {
                Answer questionAnswer = getAnswer(answer, ":");
                questionAnswers.add(questionAnswer);
            }

            if(questionAnswers.size() == 0) {
                throw new BusinessException(Errors.QUESTION_DOES_NOT_HAVE_ANSWER_OPTIONS, messageService.getLocalizedMessage("exception.question_type_not_found"));
            }

            question.setAnswers(questionAnswers);

            return question;

        }
        else if (questionStrings.length == 2) {
            throw new BusinessException(Errors.QUESTION_DOES_NOT_HAVE_ANSWER_OPTIONS, messageService.getLocalizedMessage("exception.question_does_not_have_answer_options"));
        }
        else if(questionStrings.length == 1) {
            throw new BusinessException(Errors.QUESTION_TYPE_IS_EMPTY, messageService.getLocalizedMessage("exception.question_type_is_empty"));
        }
        else {
            throw new BusinessException(Errors.QUESTION_TEXT_IS_EMPTY, messageService.getLocalizedMessage("exception.question_text_is_empty"));
        }
    }

    public Answer getAnswer(String setting, String separator) {

        if(setting == null) {
            String message = String.format("%s \"%s:true\", %s \"%s1:true, %s2:false\"), %s: null",
                    messageService.getLocalizedMessage("exception.answer_setting_is_null"),
                    messageService.getLocalizedMessage("exception.text"),
                    messageService.getLocalizedMessage("exception.example"),
                    messageService.getLocalizedMessage("exception.answer"),
                    messageService.getLocalizedMessage("exception.answer"),
                    messageService.getLocalizedMessage("exception.current_value")
            );
            throw new BusinessException(Errors.ANSWER_SETTING_IS_NULL, message);
        }
        if(separator == null) {
            throw new BusinessException(Errors.ANSWER_SEPARATOR_IS_NULL, messageService.getLocalizedMessage("exception.answer_separator_is_null"));
        }

        String[] answerStrings = setting.split(separator);

        String text = "";
        Boolean isValid = false;

        if(answerStrings.length >= 1) {
            text = answerStrings[0] != null ? (answerStrings[0]).trim() : "";
            isValid = (answerStrings.length > 1 && answerStrings[1] != null) ? (answerStrings[1]).trim().equalsIgnoreCase("true") : false;
        }

        if(text.isEmpty()) {
            throw new BusinessException(Errors.ANSWER_TEXT_IS_EMPTY, messageService.getLocalizedMessage("exception.answer_text_is_empty"));
        }

        return new Answer(text, isValid);

    }
}
