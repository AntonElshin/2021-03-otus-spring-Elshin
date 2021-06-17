package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Service для работы с выводом")
@ExtendWith(MockitoExtension.class)
public class PrintGenreServiceTest {

    @Mock
    private PrintService printService;

    @InjectMocks
    private PrintGenreServiceImpl printGenreService;

    @DisplayName("Выведет информацию о жанре")
    @Test
    void printGenre() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        Genre genre = new Genre(1L, "Жанр", "Описание");

        String str = printGenreService.printGenre(genre);

        assertAll(str,
                () -> str.contains("Жанр"),
                () -> str.contains("Описание")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

    @DisplayName("Выведет информацию о жанрах")
    @Test
    void printGenres() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        Genre genre_1 = new Genre(1L, "Жанр 1", "Описание 1");
        Genre genre_2 = new Genre(2L, "Жанр 2", "Описание 2");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre_1);
        genres.add(genre_2);

        String str = printGenreService.printGenres(genres);

        assertAll(str,
                () -> str.contains("List of genres:"),
                () -> str.contains("Жанр 1"),
                () -> str.contains("Описание 1"),
                () -> str.contains("Жанр 2"),
                () -> str.contains("Описание 2")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

    @DisplayName("Выведет информацию о количестве жанров")
    @Test
    void printGenresCount() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        String str = printGenreService.printGenresCount(0L);

        assertAll(str,
                () -> str.contains("Genres count:"),
                () -> str.contains("0")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

}
