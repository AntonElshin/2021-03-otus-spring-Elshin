package ru.otus.homework.loaders;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.service.MessageService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import static org.mockito.BDDMockito.given;

@DisplayName("Класс LoaderCSV")
@ExtendWith(MockitoExtension.class)
public class LoaderCSVTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private LoaderCSV loader;

    @DisplayName("загрузит строки из файла в ресурсах")
    @Test
    void loadQuestionsTest() {

        given(messageService.getLanguageTag()).willReturn("");

        List<String> questions = loader.loadQuestions("questions.csv");

        assertThat(questions).hasSize(5);

        for(String question : questions) {
            assertAll(question,
                    () -> Assert.assertNotNull(question),
                    () -> assertThat(question).isNotBlank()
            );
        }
    }

    @DisplayName("вернёт список доступных локалей из бандла")
    @Test
    void loadAvailableLocales() {

        loader.setLanguageResourceFolderName("i18n");
        List<String> locales = loader.loadAvailableLocales();

        assertThat(locales).hasSize(2);

        for(String locale : locales) {
            assertAll(locale,
                    () -> Assert.assertNotNull(locale),
                    () -> assertThat(locale).isNotBlank()
            );
        }

        assertThat(locales.get(0)).isEqualTo("en-US");
        assertThat(locales.get(1)).isEqualTo("ru-RU");

    }

}
