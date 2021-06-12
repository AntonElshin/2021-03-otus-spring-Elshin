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
import ru.otus.homework.exceptions.BusinessException;
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

    @Mock
    private PrintAuthorService printAuthorService;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @DisplayName("добавить автора")
    @Test
    void addAuthor() {

        Author author = new Author("Фамилия", "Имя", "Отчество");
        Author createdAuthor = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.save(author)).willReturn(createdAuthor);

        authorService.add("Фамилия", "Имя", "Отчество");

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);

    }

    @DisplayName("не создать автора из-за null в фамилии")
    @Test
    void addAuthorNullLastNameError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.add(null, "Имя", "Отчество");

        });

    }

    @DisplayName("не создать автора из-за пустой фамилии")
    @Test
    void addAuthorEmptyLastNameError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.add("", "Имя", "Отчество");

        });

    }

    @DisplayName("не создать автора из-за пустой фамилии без учёта пробелов")
    @Test
    void addAuthorEmptyLastNameIgnoreSpacesError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.add("  ", "Имя", "Отчество");

        });

    }

    @DisplayName("не создать автора из-за null в имени")
    @Test
    void addAuthorNullFirstNameError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.add("Фамилия", null, "Отчество");

        });

    }

    @DisplayName("не создать автора из-за пустой имени")
    @Test
    void addAuthorEmptyFirstNameError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.add("Фамилия", "", "Отчество");

        });

    }

    @DisplayName("не создать автора из-за пустой имени без учёта пробелов")
    @Test
    void addAuthorEmptyFirstNameIgnoreSpacesError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.add("Фамилия", "  ", "Отчество");

        });

    }

    @DisplayName("изменить автора")
    @Test
    void updateAuthorWithoutNameCheck() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(authorRepository.save(author)).willReturn(author);

        authorService.update(1L,"Фамилия", "Имя", "Отчество");

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);

    }

    @DisplayName("не изменить автора из-за нулевого идентификатора")
    @Test
    void updateAuthorZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.update(0, "Фамилия", "Имя", "Отчество");

        });

    }

    @DisplayName("не изменить автора из-за несуществующего идентификатора")
    @Test
    void updateAuthorInvalidIdError() {

        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.update(1, "Фамилия", "Имя", "Отчество");

        });

    }

    @DisplayName("не изменить автора при null во всех параметрах")
    @Test
    void updateAuthorWithNullData() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));

        authorService.update(1L, null, null, null);

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);

    }

    @DisplayName("не изменить автора при пустых строках во всех параметрах")
    @Test
    void updateAuthorWithEmptyData() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));

        authorService.update(1L, "", "", "");

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);

    }

    @DisplayName("не изменить автора при пустых строках во всех параметрах без учёта пробелов")
    @Test
    void updateAuthorWithEmptyDataIgnoreSpaces() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));

        authorService.update(1L, "  ", "  ", "  ");

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);

    }

    @DisplayName("вернуть существующий жанр по переданному идентификатору")
    @Test
    void getById() {

        Author foundAuthor = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.of(foundAuthor));
        given(printAuthorService.printAuthor(foundAuthor)).willReturn("");

        authorService.getById(1L);

        Mockito.verify(authorRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(printAuthorService, Mockito.times(1)).printAuthor(foundAuthor);

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
        given(printAuthorService.printAuthors(authors)).willReturn("");

        authorService.findByParams("Фамилия", "Имя", "Отчество");

        Mockito.verify(authorRepository, Mockito.times(1)).findAll(predicate);
        Mockito.verify(printAuthorService, Mockito.times(1)).printAuthors(authors);

    }

    @DisplayName("вернёт всех авторов")
    @Test
    void findAll() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        given(authorRepository.findAll()).willReturn(authors);
        given(printAuthorService.printAuthors(authors)).willReturn("");

        authorService.findAll();

        Mockito.verify(authorRepository, Mockito.times(1)).findAll();
        Mockito.verify(printAuthorService, Mockito.times(1)).printAuthors(authors);

    }

    @DisplayName("вернёт количество авторов")
    @Test
    void count() {

        given(authorRepository.count()).willReturn(0L);
        given(printAuthorService.printAuthorsCount(0L)).willReturn("");

        authorService.count();

        Mockito.verify(authorRepository, Mockito.times(1)).count();
        Mockito.verify(printAuthorService, Mockito.times(1)).printAuthorsCount(0L);

    }

}
