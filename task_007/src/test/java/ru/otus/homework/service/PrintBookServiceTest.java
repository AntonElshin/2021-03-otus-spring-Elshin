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
import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с выводом")
@ExtendWith(MockitoExtension.class)

public class PrintBookServiceTest {

    @Mock
    private PrintService printService;

    @Mock
    private PrintAuthorServiceImpl printAuthorService;

    @Mock
    private PrintGenreServiceImpl printGenreService;

    @Mock
    private PrintBookCommentServiceImpl printBookCommentService;

    @InjectMocks
    private PrintBookServiceImpl printBookService;

    @DisplayName("Выведет информацию о книге")
    @Test
    void printBook() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        Genre genre = new Genre(1L, "Жанр", "Описание жанра");

        Book book = new Book("Название", "123", "Описание книги", List.of(author), List.of(genre), null);

        BookComment bookComment_1 = new BookComment(1L, book, "Комментарий 1");
        BookComment bookComment_2 = new BookComment(2L, book, "Комментарий 2");

        book.setComments(List.of(bookComment_1, bookComment_2));

        given(printAuthorService.prepareAuthor(author)).willReturn("Фамилия Имя Отчество");
        given(printGenreService.prepareGenre(genre)).willReturn("Жанр Описание жанра");
        given(printBookCommentService.prepareBookComment(bookComment_1)).willReturn("Комментарий 1");
        given(printBookCommentService.prepareBookComment(bookComment_2)).willReturn("Комментарий 2");
        Mockito.doNothing().when(printService).print(Mockito.anyString());

        String str = printBookService.printBook(book);

        assertAll(str,
                () -> str.contains("Название"),
                () -> str.contains("123"),
                () -> str.contains("Описание книги"),
                () -> str.contains("Фамилия"),
                () -> str.contains("Имя"),
                () -> str.contains("Отчество"),
                () -> str.contains("Жанр"),
                () -> str.contains("Описание жанра"),
                () -> str.contains("Комментарий 1"),
                () -> str.contains("Комментарий 2")
        );

        Mockito.verify(printAuthorService, Mockito.times(1)).prepareAuthor(author);
        Mockito.verify(printGenreService, Mockito.times(1)).prepareGenre(genre);
        Mockito.verify(printBookCommentService, Mockito.times(1)).prepareBookComment(bookComment_1);
        Mockito.verify(printBookCommentService, Mockito.times(1)).prepareBookComment(bookComment_2);
        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

    @DisplayName("Выведет информацию о книгах")
    @Test
    void printBooks() {

        Author author_1 = new Author(1L,"Фамилия 1", "Имя 1", "Отчество 1");
        Genre genre_1 = new Genre(1L, "Жанр 1", "Описание жанра 1");

        Author author_2 = new Author(2L,"Фамилия 2", "Имя 2", "Отчество 2");
        Genre genre_2 = new Genre(1L, "Жанр 2", "Описание жанра 2");

        Book book_1 = new Book(1L,"Название 1", "123", "Описание книги 1", List.of(author_1), List.of(genre_1), null);
        Book book_2 = new Book(2L,"Название 2", "456", "Описание книги 2", List.of(author_2), List.of(genre_2), null);

        given(printAuthorService.prepareAuthor(author_1)).willReturn("Фамилия 1 Имя 1 Отчество 1");
        given(printAuthorService.prepareAuthor(author_2)).willReturn("Фамилия 2 Имя 2 Отчество 2");
        given(printGenreService.prepareGenre(genre_1)).willReturn("Жанр 1 Описание жанра 1");
        given(printGenreService.prepareGenre(genre_2)).willReturn("Жанр 2 Описание жанра 2");
        Mockito.doNothing().when(printService).print(Mockito.anyString());

        String str = printBookService.printBooks(List.of(book_1, book_2));

        assertAll(str,
                () -> str.contains("Название 1"),
                () -> str.contains("123"),
                () -> str.contains("Описание книги 1"),
                () -> str.contains("Фамилия 1"),
                () -> str.contains("Имя 1"),
                () -> str.contains("Отчество 1"),
                () -> str.contains("Жанр 1"),
                () -> str.contains("Описание жанра 1"),
                () -> str.contains("Комментарий 1"),
                () -> str.contains("Комментарий 2"),
                () -> str.contains("Название 2"),
                () -> str.contains("456"),
                () -> str.contains("Описание книги 2"),
                () -> str.contains("Фамилия 2"),
                () -> str.contains("Имя 2"),
                () -> str.contains("Отчество 2"),
                () -> str.contains("Жанр 2"),
                () -> str.contains("Описание жанра 2"),
                () -> str.contains("Комментарий 3"),
                () -> str.contains("Комментарий 4")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

    @DisplayName("Выведет информацию о количестве книг")
    @Test
    void printBooksCount() {

        Mockito.doNothing().when(printService).print(Mockito.anyString());

        String str = printBookService.printBooksCount(0L);

        assertAll(str,
                () -> str.contains("Books count:"),
                () -> str.contains("0")
        );

        Mockito.verify(printService, Mockito.times(1)).print(Mockito.anyString());

    }

}
