package ru.otus.homework.loaders;

import java.util.List;

public interface Loader {

    List<String> loadQuestions(String resourceName);

    List<String> loadAvailableLocales();

}
