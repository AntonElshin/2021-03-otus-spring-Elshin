package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Service для работы с выводом")
@ExtendWith(MockitoExtension.class)
public class PrintServiceImplTest {

    @InjectMocks
    private PrintServiceImpl printService;

    @DisplayName("Выведет информацию о жанре")
    @Test
    void printGenre() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        String str = printService.printGenre(genre);

        assertAll(str,
                () -> str.contains("Жанр"),
                () -> str.contains("Описание")
        );

    }

    @DisplayName("Выведет информацию о жанрах")
    @Test
    void printGenres() {

        Genre genre_1 = new Genre(1L, "Жанр 1", "Описание 1");
        Genre genre_2 = new Genre(2L, "Жанр 2", "Описание 2");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre_1);
        genres.add(genre_2);

        String str = printService.printGenres(genres);

        assertAll(str,
                () -> str.contains("List of genres:"),
                () -> str.contains("Жанр 1"),
                () -> str.contains("Описание 1"),
                () -> str.contains("Жанр 2"),
                () -> str.contains("Описание 2")
        );

    }

    @DisplayName("Выведет информацию о количестве жанров")
    @Test
    void printGenresCount() {

        String str = printService.printGenresCount(0);

        assertAll(str,
                () -> str.contains("Genres count:"),
                () -> str.contains("0")
        );

    }

    @DisplayName("Выведет информацию об авторе")
    @Test
    void printAuthor() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        String str = printService.printAuthor(author);

        assertAll(str,
                () -> str.contains("Фамилия"),
                () -> str.contains("Имя"),
                () -> str.contains("Отчество")
        );

    }

    @DisplayName("Выведет информацию об авторах")
    @Test
    void printAuthors() {

        Author author_1 = new Author(1L,"Фамилия 1", "Имя 1", "Отчество 1");
        Author author_2 = new Author(2L,"Фамилия 2", "Имя 2", "Отчество 2");
        List<Author> authors = new ArrayList<>();
        authors.add(author_1);
        authors.add(author_2);

        String str = printService.printAuthors(authors);

        assertAll(str,
                () -> str.contains("Фамилия 1"),
                () -> str.contains("Имя 1"),
                () -> str.contains("Отчество 1"),
                () -> str.contains("Фамилия 2"),
                () -> str.contains("Имя 2"),
                () -> str.contains("Отчество 2")
        );

    }

    @DisplayName("Выведет информацию о количестве авторов")
    @Test
    void printAuthorsCount() {

        String str = printService.printAuthorsCount(0);

        assertAll(str,
                () -> str.contains("Authors count:"),
                () -> str.contains("0")
        );

    }

    @DisplayName("Выведет информацию о книге")
    @Test
    void printBook() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        Genre genre = new Genre(1L, "Жанр", "Описание жанра");

        Book book = new Book("Название", "123", "Описание книги", List.of(author), List.of(genre));

        String str = printService.printBook(book);

        assertAll(str,
                () -> str.contains("Название"),
                () -> str.contains("123"),
                () -> str.contains("Описание книги"),
                () -> str.contains("Фамилия"),
                () -> str.contains("Имя"),
                () -> str.contains("Отчество"),
                () -> str.contains("Жанр"),
                () -> str.contains("Описание жанра")
        );

    }

    @DisplayName("Выведет информацию о книгах")
    @Test
    void printBooks() {

        Author author_1 = new Author(1L,"Фамилия 1", "Имя 1", "Отчество 1");
        Genre genre_1 = new Genre(1L, "Жанр 1", "Описание жанра 1");

        Author author_2 = new Author(2L,"Фамилия 2", "Имя 2", "Отчество 2");
        Genre genre_2 = new Genre(1L, "Жанр 2", "Описание жанра 2");

        Book book_1 = new Book(1L,"Название 1", "123", "Описание книги 1", List.of(author_1), List.of(genre_1));
        Book book_2 = new Book(2L,"Название 2", "456", "Описание книги 2", List.of(author_2), List.of(genre_2));

        String str = printService.printBooks(List.of(book_1, book_2));

        assertAll(str,
                () -> str.contains("Название 1"),
                () -> str.contains("123"),
                () -> str.contains("Описание книги 1"),
                () -> str.contains("Фамилия 1"),
                () -> str.contains("Имя 1"),
                () -> str.contains("Отчество 1"),
                () -> str.contains("Жанр 1"),
                () -> str.contains("Описание жанра 1"),
                () -> str.contains("Название 2"),
                () -> str.contains("456"),
                () -> str.contains("Описание книги 2"),
                () -> str.contains("Фамилия 2"),
                () -> str.contains("Имя 2"),
                () -> str.contains("Отчество 2"),
                () -> str.contains("Жанр 2"),
                () -> str.contains("Описание жанра 2")
        );

    }

    @DisplayName("Выведет информацию о количестве книг")
    @Test
    void printBooksCount() {

        String str = printService.printBooksCount(0);

        assertAll(str,
                () -> str.contains("Books count:"),
                () -> str.contains("0")
        );

    }

}
