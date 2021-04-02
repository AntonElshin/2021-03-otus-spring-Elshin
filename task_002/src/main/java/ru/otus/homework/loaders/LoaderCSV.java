package ru.otus.homework.loaders;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@Service
public class LoaderCSV implements Loader {

    @Override
    public List<String> loadQuestions(String resourceName) {

        List<String> questionsFromFile = new ArrayList<>();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        Scanner sсanner = new Scanner(inputStream).useDelimiter(Pattern.compile("\\r\\n|\\r|\\n"));

        try(inputStream; sсanner) {
            while(sсanner.hasNext()) {
                String question = sсanner.hasNext() ? sсanner.next() : "";
                questionsFromFile.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questionsFromFile;

    }
}
