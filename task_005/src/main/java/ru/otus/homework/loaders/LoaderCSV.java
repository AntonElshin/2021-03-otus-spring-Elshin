package ru.otus.homework.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.homework.service.MessageService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class LoaderCSV implements Loader {

    @Value("${question-book.language-resource-folder-name}")
    private String languageResourceFolderName;

    private final MessageService messageService;

    @Override
    public List<String> loadQuestions(String resourceName) {

        List<String> questionsFromFile = new ArrayList<>();
        String localizedResourceName = resourceName;

        if(!messageService.getLanguageTag().isEmpty() && !messageService.getLanguageTag().equalsIgnoreCase("en-US")) {
            String[] resourceNameStrings = resourceName.split("\\.");

            if(resourceNameStrings.length == 2) {

                String fileName = resourceNameStrings[0] + "_" + messageService.getLanguageTag() + "." +resourceNameStrings[1];
                URL url = getClass().getClassLoader().getResource(fileName);

                if(url != null) {
                    localizedResourceName = fileName;
                }

            }

        }

        if(resourceName != null && !resourceName.equalsIgnoreCase(localizedResourceName)) {
            resourceName = localizedResourceName;
        }
        else {
            if(!messageService.getLanguageTag().isEmpty() && !messageService.getLanguageTag().equalsIgnoreCase("en-US")) {
                System.out.println(messageService.getLocalizedMessage("messages.file_not_found_for_locale"));
            }
        }

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

    @Override
    public List<String> loadAvailableLocales() {

        Locale.setDefault(new Locale("en", "US"));

        URL url = getClass().getClassLoader().getResource(languageResourceFolderName);

        List<String> locales = new ArrayList<>();

        if(url != null) {
            File dir = new File(url.getFile());
            File[] files = dir.listFiles();

            for(File file : files) {

                String fileName = file.getName();

                if(fileName != null && fileName.toLowerCase().contains("messages_")) {
                    locales.add(fileName
                            .replace("messages_", "")
                            .replace(".properties", "")
                            .replace("_", "-"));
                }
                else {
                    locales.add(Locale.getDefault().toLanguageTag());
                }
            }
        }
        else {
            locales.add(Locale.getDefault().toLanguageTag());
        }

        return locales;
    }

    public void setLanguageResourceFolderName(String languageResourceFolderName) {
        this.languageResourceFolderName = languageResourceFolderName;
    }
}
