package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    @Value("${question-book.language-tag}")
    private String LanguageTag = "";

    private final MessageSource messageSource;

    @Override
    public String getLocalizedMessage(String key) {
        return messageSource.getMessage(key, null, Locale.forLanguageTag(LanguageTag));
    }

    public void setLanguageTag(String languageTag) {
        LanguageTag = languageTag;
    }

    public String getLanguageTag() {
        return LanguageTag;
    }

}
