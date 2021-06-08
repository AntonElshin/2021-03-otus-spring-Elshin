package ru.otus.homework.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.domain.QAuthor;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.QBook;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.AuthorDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.mapper.AuthorMapper;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с авторами должен")
@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @DisplayName("добавить автора")
    @Test
    void addAuthor() {

        AuthorDTO authorDTO = new AuthorDTO("Фамилия", "Имя", "Отчество");
        Author author = AuthorMapper.INSTANCE.fromDto(authorDTO);
        Author createdAuthor = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.save(author)).willReturn(createdAuthor);

        authorService.add(authorDTO);

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);

    }

    @DisplayName("изменить автора")
    @Test
    void updateAuthorWithoutNameCheck() {

        AuthorDTO authorDTO = new AuthorDTO(1L, "Фамилия", "Имя", "Отчество");
        Author author = AuthorMapper.INSTANCE.fromDto(authorDTO);

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(authorRepository.save(author)).willReturn(author);

        authorService.update(1L, authorDTO);

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);

    }

    @DisplayName("не изменить автора из-за нулевого идентификатора")
    @Test
    void updateAuthorZeroIdError() {

        AuthorDTO authorDTO = new AuthorDTO(1L, "Фамилия", "Имя", "Отчество");

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.update(0, authorDTO);

        });

    }

    @DisplayName("не изменить автора из-за несуществующего идентификатора")
    @Test
    void updateAuthorInvalidIdError() {

        AuthorDTO authorDTO = new AuthorDTO(1L, "Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.update(1, authorDTO);

        });

    }

    @DisplayName("вернуть существующего автора по переданному идентификатору")
    @Test
    void getById() {

        Author foundAuthor = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.of(foundAuthor));

        authorService.getById(1L);

        Mockito.verify(authorRepository, Mockito.times(1)).findById(1L);

    }

    @DisplayName("не вернуть автора из-за нулевого идентификатора")
    @Test
    void getByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.getById(0);

        });

    }

    @DisplayName("не вернуть автора из-за несуществующего идентификатора")
    @Test
    void getByIdInvalidIdError() {

        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.getById(1L);

        });

    }

    @DisplayName("удалить автора по переданному идентификатору")
    @Test
    void deleteById() {

        Author author = new Author(1L, "Фамилия", "Имя", "Отчество");

        BooleanExpression predicate = QBook.book.authors.any().id.eq(1L);

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(bookRepository.findAll(predicate)).willReturn(null);
        Mockito.doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteById(1L);

        Mockito.verify(authorRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).findAll(predicate);
        Mockito.verify(authorRepository, Mockito.times(1)).deleteById(1L);

    }

    @DisplayName("не удалить автора из-за нулевого идентификатора")
    @Test
    void deleteByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.deleteById(0);

        });

    }

    @DisplayName("не удалить автора из-за наличия связи с книгами")
    @Test
    void deleteByIdLinkedBookCountError() {

        BooleanExpression predicate = QBook.book.authors.any().id.eq(1L);

        List<Book> books = new ArrayList<>();
        books.add(new Book());

        given(bookRepository.findAll(predicate)).willReturn(books);

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.deleteById(1L);

        });

    }

    @DisplayName("найдёт авторов по параметрам")
    @Test
    void findByParams() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        BooleanExpression predicate = QAuthor.author.lastName.containsIgnoreCase("Фамилия")
                .and(QAuthor.author.firstName.containsIgnoreCase("Имя"))
                .and(QAuthor.author.middleName.containsIgnoreCase("Отчество"));

        given(authorRepository.findAll(predicate)).willReturn(authors);

        authorService.findByParams("Фамилия", "Имя", "Отчество");

        Mockito.verify(authorRepository, Mockito.times(1)).findAll(predicate);

    }

    @DisplayName("вернёт всех авторов")
    @Test
    void findAll() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        given(authorRepository.findAll()).willReturn(authors);

        authorService.findAll();

        Mockito.verify(authorRepository, Mockito.times(1)).findAll();

    }

}
