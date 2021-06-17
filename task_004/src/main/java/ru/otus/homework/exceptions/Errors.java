package ru.otus.homework.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Errors {
    ANSWER_SETTING_IS_NULL(1, "%s"),
    ANSWER_SEPARATOR_IS_NULL(2, "%s"),
    ANSWER_TEXT_IS_EMPTY(3, "%s"),
    QUESTION_SETTING_IS_NULL(4, "%s"),
    QUESTION_SEPARATOR_IS_NULL(5, "%s"),
    QUESTION_TYPE_NOT_FOUND(6, "%s"),
    QUESTION_DOES_NOT_HAVE_ANSWER_OPTIONS(7, "%s"),
    QUESTION_LIST_IS_NULL(9, "%s"),
    QUESTION_LIST_IS_EMPTY(10, "%s"),
    ANSWER_LIST_IS_EMPTY(11, "%s"),
    QUESTION_TEXT_IS_EMPTY(12, "%s"),
    QUESTION_TYPE_IS_EMPTY(13, "%s"),
    QUESTION_AND_ANSWER_QUANTITY_NOT_EQUALS(14, "%s")
    ;

    private Integer code;
    private String message;
}
