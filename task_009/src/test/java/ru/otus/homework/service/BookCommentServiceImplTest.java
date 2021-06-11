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
import ru.otus.homework.dto.BookCommentReqDTO;
import ru.otus.homework.dto.BookCommentResDTO;
import ru.otus.homework.dto.BookCommentResListDTO;
import ru.otus.homework.dto.BookReqIdDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.mapper.BookCommentMapper;
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
    private BookCommentRepository bookCommentRepository;

    @Mock
    private BookCommentMapper bookCommentMapper;

    @InjectMocks
    private BookCommentServiceImpl bookCommentService;

    @DisplayName("добавить комментарий к книге")
    @Test
    void addBookComment() {

        BookCommentReqDTO bookCommentReqDTO = new BookCommentReqDTO(new BookReqIdDTO(1L), "Комментарий");
        BookCommentResDTO bookCommentResDTO = new BookCommentResDTO(1L, "Комментарий");

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookReqIdDTO bookReqIdDTO = new BookReqIdDTO(1L);

        BookComment bookComment = new BookComment(book, "Комментарий");
        BookComment createdBookComment = new BookComment(1L, book, "Комментарий");

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentRepository.save(bookComment)).willReturn(createdBookComment);
        given(bookCommentMapper.toDto(createdBookComment)).willReturn(bookCommentResDTO);

        bookCommentService.add(bookCommentReqDTO);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentRepository, Mockito.times(1)).save(bookComment);
        Mockito.verify(bookCommentMapper, Mockito.times(1)).toDto(createdBookComment);

    }

    @DisplayName("не добавить комментарий к книге из-за некорректного идентификатора книги")
    @Test
    void addBookCommentInvalidBookIdError() {

        BookCommentReqDTO bookCommentReqDTO = new BookCommentReqDTO(new BookReqIdDTO(1L), "Комментарий");

        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.add(bookCommentReqDTO);

        });

    }

    @DisplayName("изменить комментарий к книге на новую книгу и новый текст")
    @Test
    void updateBookCommentWithNewBookAndNewText() {

        BookCommentReqDTO bookCommentReqDTO = new BookCommentReqDTO(new BookReqIdDTO(2L), "Комментарий!!!");
        BookCommentResDTO bookCommentResDTO = new BookCommentResDTO(2L, "Комментарий!!!");

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

        given(bookCommentRepository.findById(1L)).willReturn(Optional.of(bookComment));
        given(bookRepository.findById(2L)).willReturn(Optional.of(secondBook));
        given(bookCommentMapper.fromDto(bookCommentReqDTO)).willReturn(updatedBookComment);
        given(bookCommentRepository.save(updatedBookComment)).willReturn(updatedBookComment);
        given(bookCommentMapper.toDto(updatedBookComment)).willReturn(bookCommentResDTO);

        bookCommentService.update(1L, bookCommentReqDTO);

        Mockito.verify(bookCommentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(2L);
        Mockito.verify(bookCommentMapper, Mockito.times(1)).fromDto(bookCommentReqDTO);
        Mockito.verify(bookCommentRepository, Mockito.times(1)).save(updatedBookComment);
        Mockito.verify(bookCommentMapper, Mockito.times(1)).toDto(updatedBookComment);

    }

    @DisplayName("не изменить комментарий к книге из-за нулевого идентификатора комментария к книге")
    @Test
    void updateBookCommentZeroIdError() {

        BookCommentReqDTO bookCommentReqDTO = new BookCommentReqDTO(new BookReqIdDTO(1L), "Комментарий");

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.update(0, bookCommentReqDTO);

        });

    }

    @DisplayName("не изменить комментарий к книге из-за несуществующего идентификатора книги")
    @Test
    void updateBookCommentInvalidIdError() {

        BookCommentReqDTO bookCommentReqDTO = new BookCommentReqDTO(new BookReqIdDTO(1L), "Комментарий");

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
        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookCommentService.update(1L, bookCommentReqDTO);

        });

    }

    @DisplayName("вернуть существующий комментарий к книге по переданному идентификатору")
    @Test
    void getById() {

        BookCommentResDTO bookCommentResDTO = new BookCommentResDTO(1L, "Комментарий");

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
        given(bookCommentMapper.toDto(bookComment)).willReturn(bookCommentResDTO);

        bookCommentService.getById(1L);

        Mockito.verify(bookCommentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentMapper, Mockito.times(1)).toDto(bookComment);

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

        BookCommentResListDTO bookCommentResListDTO = new BookCommentResListDTO(1L, "Комментарий");
        List<BookCommentResListDTO> bookCommentsResListDTO = new ArrayList<>();
        bookCommentsResListDTO.add(bookCommentResListDTO);

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);
        BookComment bookComment = new BookComment(1L, book, "Комментарий");
        List<BookComment> bookComments = List.of(bookComment);
        book.setComments(bookComments);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(bookCommentMapper.toListDto(bookComments)).willReturn(bookCommentsResListDTO);

        bookCommentService.findAllByBookId(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookCommentMapper, Mockito.times(1)).toListDto(bookComments);

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
