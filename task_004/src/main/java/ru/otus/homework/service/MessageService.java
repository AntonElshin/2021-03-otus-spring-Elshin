package ru.otus.homework.service;

public interface MessageService {

    String getLocalizedMessage(String key);

    void setLanguageTag(String languageTag);

    String getLanguageTag();
}
