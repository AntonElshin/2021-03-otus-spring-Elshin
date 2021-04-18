package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Locales {

    EN("locales.en", ""),
    RU("locales.ru", "ru-RU")
    ;

    private String name;
    private String code;

}
