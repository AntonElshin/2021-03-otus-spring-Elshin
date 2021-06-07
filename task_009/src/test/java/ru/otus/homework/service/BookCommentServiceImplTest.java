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
import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.dto.BookDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.mapper.BookMapper;
import ru.otus.homework.repository.BookCommentRepository;
import ru.otus.homework.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с комментариями к книге должен")
@ExtendWith(MockitoExtension.class)
public class BookCommentServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private BookCommentRepository bookCommentRepository;

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

        BookDTO bookDTO = BookMapper.INSTANCE.toDto(book);

        BookCommentDTO bookCommentDTO = new BookCommentDTO(bookDTO, "Комментарий");

        Mockito.doNothing().when(validationService).validateDTO(bookCommentDTO);
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepository.save(bookComment)).willReturn(createdBookComment);

        bookCommentService.add(bookCommentDTO);

        Mockito.verify(validationService, Mockito.times(1)).validateDTO(bookCommentDTO);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepository, Mockito.times(1)).save(bookComment);

    }

    @DisplayName("не добавить комментарий к книге из-за некорректного идентификатора книги")
    @Test
    void addBookCommentInvalidBookIdError() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);

        BookComment bookComment = new BookComment(book, "Комментарий");
        BookComment createdBookComment = new BookComment(1L, book, "Комментарий");

        BookDTO bookDTO = BookMapper.INSTANCE.toDto(book);

        BookCommentDTO bookCommentDTO = new BookCommentDTO(bookDTO, "Комментарий");

        Mockito.doNothing().when(validationService).validateDTO(bookCommentDTO);
        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.add(bookCommentDTO);

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

        BookDTO secondBookDTO = BookMapper.INSTANCE.toDto(secondBook);
        BookCommentDTO bookCommentDTO = new BookCommentDTO(secondBookDTO, "Комментарий!!!");

        Mockito.doNothing().when(validationService).validateDTO(bookCommentDTO);
        given(bookCommentRepository.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepository.findById(2L)).willReturn(Optional.of(secondBook));
        given(bookCommentRepository.save(updatedBookComment)).willReturn(updatedBookComment);

        bookCommentService.update(1L, bookCommentDTO);

        Mockito.verify(validationService, Mockito.times(1)).validateDTO(bookCommentDTO);
        Mockito.verify(bookCommentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(2L);
        Mockito.verify(bookCommentRepository, Mockito.times(1)).save(updatedBookComment);

    }

    @DisplayName("не изменить комментарий к книге из-за нулевого идентификатора комментария к книге")
    @Test
    void updateBookCommentZeroIdError() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);

        BookComment bookComment = new BookComment(book, "Комментарий");
        BookComment createdBookComment = new BookComment(1L, book, "Комментарий");

        BookDTO bookDTO = BookMapper.INSTANCE.toDto(book);

        BookCommentDTO bookCommentDTO = new BookCommentDTO(bookDTO, "Комментарий");

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.update(0, bookCommentDTO);

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

        BookDTO bookDTO = BookMapper.INSTANCE.toDto(book);

        BookCommentDTO bookCommentDTO = new BookCommentDTO(bookDTO, "Комментарий");

        Mockito.doNothing().when(validationService).validateDTO(bookCommentDTO);
        given(bookCommentRepository.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.update(1L, bookCommentDTO);

        });

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

        given(bookCommentRepository.findById(1L)).willReturn(Optional.of(bookComment));

        bookCommentService.getById(1L);

        Mockito.verify(bookCommentRepository, Mockito.times(1)).findById(1L);

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

        given(bookCommentRepository.findById(1L)).willReturn(Optional.empty());

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

        given(bookCommentRepository.findById(1L)).willReturn(Optional.of(bookComment));
        Mockito.doNothing().when(bookCommentRepository).deleteById(1L);

        bookCommentService.deleteById(1L);

        Mockito.verify(bookCommentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepository, Mockito.times(1)).deleteById(1L);

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

        given(bookCommentRepository.findById(1L)).willReturn(Optional.empty());

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

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepository.findAllByBookId(1L)).willReturn(List.of(bookComment));

        bookCommentService.findAllByBookId(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepository, Mockito.times(1)).findAllByBookId(1L);

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

        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.findAllByBookId(1L);

        });

    }

}
