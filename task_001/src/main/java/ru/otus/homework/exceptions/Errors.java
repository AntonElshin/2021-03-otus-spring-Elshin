package ru.otus.homework.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Errors {
    ANSWER_SETTING_IS_NULL(1, "Answer setting is invalid (format: \"text:isValid\", example \"Answer1:true, Answer2:false\"), current value: %s"),
    ANSWER_SEPARATOR_IS_NULL(2, "Missing separator for answers, current value: %s"),
    ANSWER_TEXT_IS_EMPTY(3, "Answer text is empty, current value: %s"),
    QUESTION_SETTING_IS_NULL(4, "Question setting is invalid (format: \"Question?,typing|single|multy,Answer:true\", example \"Question?,typing,Answer:true)\", current value: %s"),
    QUESTION_SEPARATOR_IS_NULL(5, "Missing separator for question, current value: %s"),
    QUESTION_TYPE_NOT_FOUND(6, "Question type not found: %s"),
    QUESTION_DOES_NOT_HAVE_ANSWER_OPTIONS(7, "Question does not have answer options"),
    QUESTION_LIST_IS_NULL(9, "Question list is null"),
    QUESTION_LIST_IS_EMPTY(10, "Question book should have questions")
    ;

    private Integer code;
    private String message;
}
