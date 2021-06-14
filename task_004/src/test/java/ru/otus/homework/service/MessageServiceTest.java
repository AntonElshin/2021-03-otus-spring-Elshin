package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс LoaderCSV")
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MessageServiceImpl messageService;

    @DisplayName("метод getLocalizedMessage запросит строку в английской локали")
    @Test
    void getLocalizedMessageTest_Default() {

        messageService.setLanguageTag("");
        given(messageSource.getMessage("some key", null, Locale.forLanguageTag(""))).willReturn("some localized string");

        messageService.getLocalizedMessage("some key");

        Mockito.verify(messageSource, Mockito.times(1))
                .getMessage("some key", null, Locale.forLanguageTag(""));
    }

    @DisplayName("метод getLocalizedMessage запросит строку в русской локали")
    @Test
    void getLocalizedMessageTest_Russian() {

        messageService.setLanguageTag("ru-RU");
        given(messageSource.getMessage("some key", null, Locale.forLanguageTag("ru-RU"))).willReturn("some localized string");

        messageService.getLocalizedMessage("some key");

        Mockito.verify(messageSource, Mockito.times(1))
                .getMessage("some key", null, Locale.forLanguageTag("ru-RU"));
    }

    @DisplayName("метод setLanguageTag установит русский язык")
    @Test
    void setLanguageTagTest() {

        messageService.setLanguageTag("ru-RU");

        assertThat(messageService.getLanguageTag()).isEqualTo("ru-RU");

    }

    @DisplayName("метод getLanguageTag получит установленный русский язык")
    @Test
    void getLanguageTagTest() {

        messageService.setLanguageTag("ru-RU");

        assertThat(messageService.getLanguageTag()).isEqualTo("ru-RU");

    }

}
