package ru.otus.homework.service;

import org.junit.jupiter.api.Assertions;
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
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.repository.BookCommentRepositoryJpa;
import ru.otus.homework.repository.BookRepositoryJpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с комментариями к книге должен")
@ExtendWith(MockitoExtension.class)
public class BookCommentServiceImplTest {

    @Mock
    private BookRepositoryJpa bookRepositoryJpa;

    @Mock
    private PrintService printService;

    @Mock
    private BookCommentRepositoryJpa bookCommentRepositoryJpa;

    @InjectMocks
    private BookCommentServiceImpl bookCommentService;

    @DisplayName("добавить комментарий к книге")
    @Test
    void addBookComment() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);

        BookComment bookComment = new BookComment(book, "Комментарий");
        BookComment createdBookComment = new BookComment(1L, book, "Комментарий");

        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepositoryJpa.save(bookComment)).willReturn(createdBookComment);

        bookCommentService.add(1L, "Комментарий");

        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).save(bookComment);

    }

    @DisplayName("не добавить комментарий к книге из-за null в идентификаторе книги")
    @Test
    void addBookCommentNullBookIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.add(null, "Комментарий");

        });

    }

    @DisplayName("не добавить комментарий к книге из-за null в комментарии")
    @Test
    void addBookCommentNullTextError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.add(1L, null);

        });

    }

    @DisplayName("не добавить комментарий к книге из-за пустой строки в комментарии")
    @Test
    void addBookCommentEmptyStringTextError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.add(1L, "");

        });

    }

    @DisplayName("не добавить комментарий к книге из-за пустой строки в комментарии без учёта пробелов")
    @Test
    void addBookCommentEmptyStringTextIgnoreSpacesError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.add(1L, "  ");

        });

    }

    @DisplayName("не добавить комментарий к книге из-за некорректного идентификатора книги")
    @Test
    void addBookCommentInvalidBookIdError() {

        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.add(1L, "Комментарий");

        });

    }

    @DisplayName("изменить комментарий к книге на новую книгу и новый текст")
    @Test
    void updateBookCommentWithNewBookAndNewText() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        Book secondBook = new Book(2L,"Название 2", null, "Описание 2", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        BookComment updatedBookComment = new BookComment(1L, secondBook, "Комментарий!!!");
        book.setComments(List.of(bookComment));

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepositoryJpa.findById(2L)).willReturn(Optional.of(secondBook));
        given(bookCommentRepositoryJpa.save(updatedBookComment)).willReturn(updatedBookComment);

        bookCommentService.update(1L, 2L, "Комментарий!!!");

        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findById(2L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).save(updatedBookComment);

    }

    @DisplayName("изменить комментарий к книге на новую книгу")
    @Test
    void updateBookCommentWithNewBook() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        Book secondBook = new Book(2L,"Название 2", null, "Описание 2", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        BookComment updatedBookComment = new BookComment(1L, secondBook, "Комментарий");
        book.setComments(List.of(bookComment));

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepositoryJpa.findById(2L)).willReturn(Optional.of(secondBook));
        given(bookCommentRepositoryJpa.save(updatedBookComment)).willReturn(updatedBookComment);

        bookCommentService.update(1L, 2L, null);

        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findById(2L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).save(updatedBookComment);

    }

    @DisplayName("изменить комментарий к книге на новый текст")
    @Test
    void updateBookCommentWithNewText() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        BookComment updatedBookComment = new BookComment(1L, book, "Комментарий!!!");
        book.setComments(List.of(bookComment));

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookCommentRepositoryJpa.save(updatedBookComment)).willReturn(updatedBookComment);

        bookCommentService.update(1L, null, "Комментарий!!!");

        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).save(updatedBookComment);

    }

    @DisplayName("не изменить комментарий к книге из-за нулевого идентификатора комментария к книге")
    @Test
    void updateBookCommentZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.update(0, 1L, "Комментарий!!!");

        });

    }

    @DisplayName("не изменить комментарий к книге из-за несуществующего идентификатора книги")
    @Test
    void updateBookCommentInvalidIdError() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        book.setComments(List.of(bookComment));
        
        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepositoryJpa.findById(2L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.update(1L, 2L, "Комментарий");

        });

    }

    @DisplayName("не изменить комментарий к книге при null во всех параметрах")
    @Test
    void updateBookCommentWithNullData() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        book.setComments(List.of(bookComment));

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepositoryJpa.save(bookComment)).willReturn(bookComment);

        bookCommentService.update(1L, 1L, "Комментарий");

        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).save(bookComment);
        
    }

    @DisplayName("не изменить комментарий к книге при пустом тексте комментария")
    @Test
    void updateBookCommentWithEmptyText() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        book.setComments(List.of(bookComment));

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepositoryJpa.save(bookComment)).willReturn(bookComment);

        bookCommentService.update(1L, 1L, "");

        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).save(bookComment);

    }

    @DisplayName("не изменить комментарий к книге при пустом тексте комментария без учёта пробелов")
    @Test
    void updateBookCommentWithEmptyTextIgnoreSpaces() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        book.setComments(List.of(bookComment));

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepositoryJpa.save(bookComment)).willReturn(bookComment);

        bookCommentService.update(1L, 1L, "   ");

        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).save(bookComment);

    }

    @DisplayName("вернуть существующий комментарий к книге по переданному идентификатору")
    @Test
    void getById() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        book.setComments(List.of(bookComment));

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        given(printService.printBookComment(bookComment)).willReturn("");

        bookCommentService.getById(1L);

        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(printService, Mockito.times(1)).printBookComment(bookComment);

    }

    @DisplayName("не вернуть комментарий к книге из-за нулевого идентификатора")
    @Test
    void getByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.getById(0);

        });

    }

    @DisplayName("не вернуть комментарий к книге из-за несуществующего идентификатора")
    @Test
    void getByIdInvalidIdError() {

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.getById(1L);

        });

    }

    @DisplayName("удалить комментарий к книге по переданному идентификатору")
    @Test
    void deleteById() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        book.setComments(List.of(bookComment));

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.of(bookComment));
        Mockito.doNothing().when(bookCommentRepositoryJpa).deleteById(1L);

        bookCommentService.deleteById(1L);

        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).deleteById(1L);

    }

    @DisplayName("не удалить комментарий к книге из-за нулевого идентификатора")
    @Test
    void deleteByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.deleteById(0);

        });

    }

    @DisplayName("не удалить комментарий к книге из-за некорректного идентификатора")
    @Test
    void deleteByIdInvalidIdError() {

        given(bookCommentRepositoryJpa.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.deleteById(1L);

        });

    }

    @DisplayName("найдёт комментарии к книге по идентификатору книги")
    @Test
    void findAllByBookId() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        book.setComments(List.of(bookComment));

        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepositoryJpa.findAllByBookId(1L)).willReturn(List.of(bookComment));
        given(printService.printBookComments(List.of(bookComment))).willReturn("");

        bookCommentService.findAllByBookId(1L);

        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).findAllByBookId(1L);
        Mockito.verify(printService, Mockito.times(1)).printBookComments(List.of(bookComment));

    }

    @DisplayName("не найдёт комментарии к книге из-за нулевого идентификатора книги")
    @Test
    void findAllByBookIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.findAllByBookId(0);

        });

    }

    @DisplayName("не найдёт комментарии к книге из-за некорректного идентификатора книги")
    @Test
    void findAllByBookIdInvalidIdError() {

        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.findAllByBookId(1L);

        });

    }

    @DisplayName("вернёт количество комментариев к книге по идентификатору книги")
    @Test
    void countByBookId() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        book.setComments(List.of(bookComment));

        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepositoryJpa.countByBookId(1L)).willReturn(0L);
        given(printService.printBookCommentsCount(0L)).willReturn("");

        bookCommentService.countByBookId(1L);

        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepositoryJpa, Mockito.times(1)).countByBookId(1L);
        Mockito.verify(printService, Mockito.times(1)).printBookCommentsCount(0L);

    }

    @DisplayName("не вернёт количество комментариев к книге из-за нулевого идентификатора книги")
    @Test
    void countByBookIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.countByBookId(0);

        });

    }

    @DisplayName("не вернёт количество комментариев к книге из-за некорректного идентификатора книги")
    @Test
    void countByBookIdInvalidIdError() {

        given(bookRepositoryJpa.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.countByBookId(1L);

        });

    }

}
