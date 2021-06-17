package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionTypes {

    TYPING("typing",
            "enums.typing.hint",
            "enums.typing.question_example",
            "enums.typing.answer_example"),
    SINGLE("single",
            "enums.single.hint",
            "enums.single.question_example",
            "enums.single.answer_example"),
    MULTY("multy",
            "enums.multy.hint",
            "enums.multy.question_example",
            "enums.multy.answer_example")
    ;

    private String name;
    private String questionHint;
    private String questionExample;
    private String answerExample;

    public static QuestionTypes getByName(String name) {

        for(QuestionTypes questionType : QuestionTypes.values()) {
            if(questionType.getName().equalsIgnoreCase(name)) {
                return questionType;
            }
        }

        return null;
    }

}
