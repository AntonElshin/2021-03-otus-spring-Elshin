package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.domain.Author;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Service для работы с выводом")
@ExtendWith(MockitoExtension.class)
public class PrintAuthorServiceTest {

    @Mock
    private PrintService printService;

    @InjectMocks
    private PrintAuthorServiceImpl printAuthorService;

    @DisplayName("Выведет информацию об авторе")
    @Test
    void printAuthor() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        String str = printAuthorService.printAuthor(author);

        assertAll(str,
                () -> str.contains("Фамилия"),
                () -> str.contains("Имя"),
                () -> str.contains("Отчество")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

    @DisplayName("Выведет информацию об авторах")
    @Test
    void printAuthors() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        Author author_1 = new Author(1L,"Фамилия 1", "Имя 1", "Отчество 1");
        Author author_2 = new Author(2L,"Фамилия 2", "Имя 2", "Отчество 2");
        List<Author> authors = new ArrayList<>();
        authors.add(author_1);
        authors.add(author_2);

        String str = printAuthorService.printAuthors(authors);

        assertAll(str,
                () -> str.contains("Фамилия 1"),
                () -> str.contains("Имя 1"),
                () -> str.contains("Отчество 1"),
                () -> str.contains("Фамилия 2"),
                () -> str.contains("Имя 2"),
                () -> str.contains("Отчество 2")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

    @DisplayName("Выведет информацию о количестве авторов")
    @Test
    void printAuthorsCount() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        String str = printAuthorService.printAuthorsCount(0L);

        assertAll(str,
                () -> str.contains("Authors count:"),
                () -> str.contains("0")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

}
