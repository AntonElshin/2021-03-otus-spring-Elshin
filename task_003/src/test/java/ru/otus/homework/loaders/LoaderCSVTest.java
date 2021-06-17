package ru.otus.homework.loaders;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Класс LoaderCSV")
@ExtendWith(MockitoExtension.class)
public class LoaderCSVTest {

    @InjectMocks
    private LoaderCSV loader;

    @DisplayName("загрузит строки из файла в ресурсах")
    @Test
    void loadQuestionsTest() throws Exception {

        List<String> questions = loader.loadQuestions("questions.csv");

        assertThat(questions).hasSize(5);

        for(String question : questions) {
            assertAll(question,
                    () -> Assert.assertNotNull(question),
                    () -> assertThat(question).isNotBlank()
            );
        }
    }

}
