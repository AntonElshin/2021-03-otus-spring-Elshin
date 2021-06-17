package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.BookComment;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Service для работы с выводом")
@ExtendWith(MockitoExtension.class)
public class PrintBookCommentServiceTest {

    @Mock
    private PrintService printService;

    @InjectMocks
    private PrintBookCommentServiceImpl printBookCommentService;

    @DisplayName("Выведет информацию о комментарии к книге")
    @Test
    void printBookComment() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        Genre genre = new Genre(1L, "Жанр", "Описание жанра");
        Book book = new Book(1L,"Название", "123", "Описание книги", List.of(author), List.of(genre), null);

        BookComment bookComment = new BookComment(1L, book, "Интересная!");

        String str = printBookCommentService.printBookComment(bookComment);

        assertAll(str,
                () -> str.contains("1"),
                () -> str.contains("Интересная!")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

    @DisplayName("Выведет информацию о комментариях к книге")
    @Test
    void printBookComments() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        Genre genre = new Genre(1L, "Жанр", "Описание жанра");
        Book book = new Book(1L,"Название", "123", "Описание книги", List.of(author), List.of(genre), null);

        BookComment bookComment_1 = new BookComment(1L, book, "Интересная!");
        BookComment bookComment_2 = new BookComment(2L, book, "Скучная!");

        String str = printBookCommentService.printBookComments(List.of(bookComment_1, bookComment_2));

        assertAll(str,
                () -> str.contains("1"),
                () -> str.contains("Интересная!"),
                () -> str.contains("2"),
                () -> str.contains("Скучная!")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

    @DisplayName("Выведет информацию о количестве комментариев к книге")
    @Test
    void printBookCommentsCount() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        String str = printBookCommentService.printBookCommentsCount(0L);

        assertAll(str,
                () -> str.contains("Book comments count:"),
                () -> str.contains("0")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

}
