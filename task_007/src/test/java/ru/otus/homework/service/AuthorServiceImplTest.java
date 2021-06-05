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
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.repository.AuthorRepositoryJpa;
import ru.otus.homework.repository.BookRepositoryJpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с авторами должен")
@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Mock
    private BookRepositoryJpa bookRepositoryJpa;

    @Mock
    private PrintService printService;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @DisplayName("добавить автора")
    @Test
    void addAuthor() {

        Author author = new Author("Фамилия", "Имя", "Отчество");
        Author createdAuthor = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepositoryJpa.save(author)).willReturn(createdAuthor);

        authorService.add("Фамилия", "Имя", "Отчество");

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).save(author);

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

        given(authorRepositoryJpa.findById(1L)).willReturn(Optional.of(author));
        given(authorRepositoryJpa.save(author)).willReturn(author);

        authorService.update(1L,"Фамилия", "Имя", "Отчество");

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).save(author);

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

        given(authorRepositoryJpa.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.update(1, "Фамилия", "Имя", "Отчество");

        });

    }

    @DisplayName("не изменить автора при null во всех параметрах")
    @Test
    void updateAuthorWithNullData() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepositoryJpa.findById(1L)).willReturn(Optional.of(author));

        authorService.update(1L, null, null, null);

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).save(author);

    }

    @DisplayName("не изменить автора при пустых строках во всех параметрах")
    @Test
    void updateAuthorWithEmptyData() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepositoryJpa.findById(1L)).willReturn(Optional.of(author));

        authorService.update(1L, "", "", "");

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).save(author);

    }

    @DisplayName("не изменить автора при пустых строках во всех параметрах без учёта пробелов")
    @Test
    void updateAuthorWithEmptyDataIgnoreSpaces() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepositoryJpa.findById(1L)).willReturn(Optional.of(author));

        authorService.update(1L, "  ", "  ", "  ");

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).save(author);

    }

    @DisplayName("вернуть существующий жанр по переданному идентификатору")
    @Test
    void getById() {

        Author foundAuthor = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepositoryJpa.findById(1L)).willReturn(Optional.of(foundAuthor));
        given(printService.printAuthor(foundAuthor)).willReturn("");

        authorService.getById(1L);

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).findById(1L);
        Mockito.verify(printService, Mockito.times(1)).printAuthor(foundAuthor);

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

        given(authorRepositoryJpa.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.getById(1L);

        });

    }

    @DisplayName("удалить автора по переданному идентификатору")
    @Test
    void deleteById() {

        given(bookRepositoryJpa.findByParamsEqualsIgnoreCase(null, null, 1L, null)).willReturn(null);
        Mockito.doNothing().when(authorRepositoryJpa).deleteById(1L);

        authorService.deleteById(1L);

        Mockito.verify(bookRepositoryJpa, Mockito.times(1)).findByParamsEqualsIgnoreCase(null, null, 1L, null);
        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).deleteById(1L);

    }

    @DisplayName("не удалить автора из-за нулевого идентификатора")
    @Test
    void deleteByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.deleteById(0);

        });

    }

    @DisplayName("не удалить жанр из-за наличия связи с книгами")
    @Test
    void deleteByIdLinkedBookCountError() {

        List<Book> books = new ArrayList<>();
        books.add(new Book());

        given(bookRepositoryJpa.findByParamsEqualsIgnoreCase(null, null, 1L, null)).willReturn(books);


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

        given(authorRepositoryJpa.findByParamsLikeIgnoreCase("Фамилия", "Имя", "Отчество")).willReturn(authors);
        given(printService.printAuthors(authors)).willReturn("");

        authorService.findByParams("Фамилия", "Имя", "Отчество");

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).findByParamsLikeIgnoreCase("Фамилия", "Имя", "Отчество");
        Mockito.verify(printService, Mockito.times(1)).printAuthors(authors);

    }

    @DisplayName("вернёт всех авторов")
    @Test
    void findAll() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        given(authorRepositoryJpa.findAll()).willReturn(authors);
        given(printService.printAuthors(authors)).willReturn("");

        authorService.findAll();

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).findAll();
        Mockito.verify(printService, Mockito.times(1)).printAuthors(authors);

    }

    @DisplayName("вернёт количество авторов")
    @Test
    void count() {

        given(authorRepositoryJpa.count()).willReturn(0L);
        given(printService.printAuthorsCount(0L)).willReturn("");

        authorService.count();

        Mockito.verify(authorRepositoryJpa, Mockito.times(1)).count();
        Mockito.verify(printService, Mockito.times(1)).printAuthorsCount(0L);

    }

}
