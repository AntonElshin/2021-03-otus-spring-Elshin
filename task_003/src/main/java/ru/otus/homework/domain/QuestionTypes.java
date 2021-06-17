package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionTypes {

    TYPING("typing",
            "Please type on keyboard correct answer (case insensitive)",
            "Question?",
            "Our answer: Answer"),
    SINGLE("single",
            "Please type on keyboard single correct option number",
            "Question?   1.Option   2.Option   3.Option",
            "Our answer: 2"),
    MULTY("multy",
            "Please type on keyboard correct option numbers through space",
            "Question?   1.Option   2.Option   3.Option",
            "Our answer: 1 2")
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
