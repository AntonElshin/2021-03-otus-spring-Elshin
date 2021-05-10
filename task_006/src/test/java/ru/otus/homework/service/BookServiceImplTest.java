package ru.otus.homework.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с книгами должен")
@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private AuthorDao authorDao;

    @Mock
    private GenreDao genreDao;

    @Mock
    private BookDao bookDao;

    @Mock
    private PrintService printService;

    @InjectMocks
    private BookServiceImpl bookService;

    @DisplayName("добавить книгу c проверкой ISBN")
    @Test
    void addBookWithIsbnCheck() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book("Название", "123", "Описание", authors, genres);

        given(bookDao.getByParamsEqualsIgnoreCase(null, "123", null, null)).willReturn(new ArrayList<>());
        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).insert(0, book);

        bookService.add("Название", "123", "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(1)).getByParamsEqualsIgnoreCase(null, "123", null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).insert(0, book);

    }

    @DisplayName("добавить книгу без проверкой ISBN со значением null в ISBN")
    @Test
    void addBookWithoutIsbnCheckNullIsbn() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book("Название", null, "Описание", authors, genres);

        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).insert(0, book);

        bookService.add("Название", null, "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(0)).getByParamsEqualsIgnoreCase(null, null, null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).insert(0, book);

    }

    @DisplayName("добавить книгу без проверкой ISBN со пустым значением ISBN")
    @Test
    void addBookWithoutIsbnCheckEmptyIsbn() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book("Название", "", "Описание", authors, genres);

        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).insert(0, book);

        bookService.add("Название", "", "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(0)).getByParamsEqualsIgnoreCase(null, "", null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).insert(0, book);

    }

    @DisplayName("добавить книгу без проверкой ISBN со пустым значением ISBN без учёта пробелов")
    @Test
    void addBookWithoutIsbnCheckEmptyIsbnIgnoreSpaces() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book("Название", "  ", "Описание", authors, genres);

        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).insert(0, book);

        bookService.add("Название", "  ", "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(0)).getByParamsEqualsIgnoreCase(null, "  ", null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).insert(0, book);

    }

    @DisplayName("не создать книгу из-за null в названии")
    @Test
    void addBookNullTitleError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add(null, "123", "Описание", "1", "1");

        });

    }

    @DisplayName("не создать книгу из-за пустого названия")
    @Test
    void addBookEmptyTitleError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("", "123", "Описание", "1", "1");

        });

    }

    @DisplayName("не создать книгу из-за пустого названия без учёта пробелов")
    @Test
    void addBookEmptyTitleIgnoreSpacesError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("   ", "123", "Описание", "1", "1");

        });

    }

    @DisplayName("не создать книгу из-за null в авторах")
    @Test
    void addBookNullAuthorsError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", null, "1");

        });

    }

    @DisplayName("не создать книгу из-за пустой строки авторов")
    @Test
    void addBookEmptyAuthorsError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", "", "1");

        });

    }

    @DisplayName("не создать книгу из-за пустой строки авторов без учёта пробелов")
    @Test
    void addBookEmptyAuthorsIgnoreSpacesError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", "  ", "1");

        });

    }

    @DisplayName("не создать книгу из-за null в жанрах")
    @Test
    void addBookNullGenresError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", "1", null);

        });

    }

    @DisplayName("не создать книгу из-за пустой строки жанров")
    @Test
    void addBookEmptyGenresError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", "1", "");

        });

    }

    @DisplayName("не создать книгу из-за пустой строки жанров без учёта пробелов")
    @Test
    void addBookEmptyGenresIgnoreSpacesError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", "1", "   ");

        });

    }

    @DisplayName("не создать книгу из-за неуникального ISBN")
    @Test
    void addBookNotUniqueIsbnError() {

        Book book = new Book("Название", "123", "Описание", new ArrayList<>(), new ArrayList<>());
        List<Book> books = new ArrayList<>();
        books.add(book);

        given(bookDao.getByParamsEqualsIgnoreCase(null, "123", null, null)).willReturn(books);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", "1", "1");

        });

    }

    @DisplayName("не создать книгу из-за несуществующих авторов")
    @Test
    void addBookNotInvalidAuthors() {

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(bookDao.getByParamsEqualsIgnoreCase(null, "123", null, null)).willReturn(new ArrayList<>());
        given(authorDao.getByIds(List.of("3"))).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", "3", "1");

        });

    }

    @DisplayName("не создать книгу из-за несуществующих жанров")
    @Test
    void addBookNotInvalidGenres() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        given(bookDao.getByParamsEqualsIgnoreCase(null, "123", null, null)).willReturn(new ArrayList<>());
        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("3"))).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.add("Название", "123", "Описание", "1", "3");

        });

    }

    @DisplayName("изменить жанр без проверки уникальности ISBN со значением null в ISBN")
    @Test
    void updateBookWithoutIsbnCheckNullIsbn() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", null, "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);
        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).update(1L, book);

        bookService.update(1L, "Название", null, "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(0)).getByParamsEqualsIgnoreCase(null, null, null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).update(1L, book);

    }

    @DisplayName("изменить жанр без проверки уникальности ISBN со пустым значением ISBN")
    @Test
    void updateBookWithoutIsbnCheckEmptyIsbn() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", "", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);
        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).update(1L, book);

        bookService.update(1L, "Название", "", "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(0)).getByParamsEqualsIgnoreCase(null, "", null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).update(1L, book);

    }

    @DisplayName("изменить жанр без проверки уникальности ISBN со пустым значением ISBN без учёта пробелов")
    @Test
    void updateBookWithoutIsbnCheckEmptyIsbnIgnoreSpaces() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", "  ", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);
        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).update(1L, book);

        bookService.update(1L, "Название", "  ", "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(0)).getByParamsEqualsIgnoreCase(null, "  ", null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).update(1L, book);

    }

    @DisplayName("изменить жанр без проверки уникальности ISBN")
    @Test
    void updateBookWithoutIsbnCheck() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L,"Название", "123", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);
        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).update(1L, book);

        bookService.update(1L, "Название", "123", "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(0)).getByParamsEqualsIgnoreCase(null, "123", null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).update(1L, book);

    }

    @DisplayName("изменить жанр с проверки уникальности ISBN")
    @Test
    void updateBookWithIsbnCheck() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book foundBook = new Book(1L, "Название", "123", "Описание", authors, genres);
        Book updateBook = new Book(1L, "Название", "1234", "Описание", authors, genres);
        List<Book> books = new ArrayList<>();
        books.add(foundBook);

        given(bookDao.getById(1L)).willReturn(foundBook);
        given(bookDao.getByParamsEqualsIgnoreCase(null, "1234", null, null)).willReturn(new ArrayList<>());
        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1"))).willReturn(genres);
        Mockito.doNothing().when(bookDao).update(1L, updateBook);

        bookService.update(1L, "Название", "1234", "Описание", "1", "1");

        Mockito.verify(bookDao, Mockito.times(1)).getByParamsEqualsIgnoreCase(null, "1234", null, null);
        Mockito.verify(authorDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(genreDao, Mockito.times(1)).getByIds(List.of("1"));
        Mockito.verify(bookDao, Mockito.times(1)).update(1L, updateBook);

    }

    @DisplayName("не изменить книгу из-за нулевого идентификатора")
    @Test
    void updateBookZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.update(0, "Название", "123", "Описание", "1", "1");

        });

    }

    @DisplayName("не изменить книгу из-за несуществующего идентификатора")
    @Test
    void updateBookInvalidIdError() {

        given(bookDao.getById(1L)).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.update(1, "Название", "123", "Описание", "1", "1");

        });

    }

    @DisplayName("не изменить книгу из-за неуникального ISBN")
    @Test
    void updateBookNotUniqueNameError() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book foundBook = new Book(1L, "Название", "123", "Описание", authors, genres);
        Book book = new Book(1L, "Название", "1234", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(foundBook);
        given(bookDao.getByParamsEqualsIgnoreCase(null, "1234", null, null)).willReturn(List.of(foundBook));

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.update(1, "Название", "1234", "Описание", "1", "1");

        });

    }

    @DisplayName("не изменить книгу при null во всех параметрах")
    @Test
    void updateBookWithNullData() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);

        bookService.update(1L, null, null, null, null, null);

        Mockito.verify(bookDao, Mockito.times(0)).update(1L, book);

    }

    @DisplayName("не изменить книгу при пустых строках во всех параметрах")
    @Test
    void updateBookWithEmptyData() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);

        bookService.update(1L, "", "", "", "", "");

        Mockito.verify(bookDao, Mockito.times(0)).update(1L, book);

    }

    @DisplayName("не изменить книгу при пустых строках во всех параметрах без учёта пробелов")
    @Test
    void updateBookWithEmptyDataIgnoreSpaces() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);

        bookService.update(1L, "  ", "  ", "  ", "  ", "  ");

        Mockito.verify(bookDao, Mockito.times(0)).update(1L, book);

    }

    @DisplayName("не изменить книгу из-за несуществующих авторов")
    @Test
    void updateBookNotInvalidAuthors() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);
        given(authorDao.getByIds(List.of("2"))).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.update(1L, "Название", "123", "Описание", "2", "1");

        });

    }

    @DisplayName("не изменить книгу из-за несуществующих жанров")
    @Test
    void updateBookNotInvalidGenres() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);
        given(authorDao.getByIds(List.of("1"))).willReturn(authors);
        given(genreDao.getByIds(List.of("2"))).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.update(1L, "Название", "123", "Описание", "1", "2");

        });

    }

    @DisplayName("вернуть существующий книгу по переданному идентификатору")
    @Test
    void getById() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);
        given(printService.printBook(book)).willReturn("");

        bookService.getById(1L);

        Mockito.verify(bookDao, Mockito.times(1)).getById(1L);
        Mockito.verify(printService, Mockito.times(1)).printBook(book);

    }

    @DisplayName("не вернуть книгу из-за нулевого идентификатора")
    @Test
    void getByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.getById(0);

        });

    }

    @DisplayName("не вернуть книгу из-за несуществующего идентификатора")
    @Test
    void getByIdInvalidIdError() {

        given(bookDao.getById(1L)).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.getById(1L);

        });

    }

    @DisplayName("удалить книгу по переданному идентификатору")
    @Test
    void deleteById() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);

        given(bookDao.getById(1L)).willReturn(book);
        Mockito.doNothing().when(bookDao).deleteById(1L);

        bookService.deleteById(1L);

        Mockito.verify(bookDao, Mockito.times(1)).getById(1L);
        Mockito.verify(bookDao, Mockito.times(1)).deleteById(1L);

    }

    @DisplayName("не удалить книгу из-за нулевого идентификатора")
    @Test
    void deleteByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.deleteById(0);

        });

    }

    @DisplayName("не удалить книгу из-за несуществующего идентификатора")
    @Test
    void deleteByIdInvalidIdError() {

        given(bookDao.getById(1L)).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.deleteById(1L);

        });

    }

    @DisplayName("найдёт книги по параметрам")
    @Test
    void findByParams() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);
        List<Book> books = new ArrayList<>();
        books.add(book);

        given(bookDao.getByParamsLikeIgnoreCase("Название", "123", 1L, 1L)).willReturn(books);
        given(printService.printBooks(books)).willReturn("");

        bookService.findByParams("Название", "123", 1L, 1L);

        Mockito.verify(bookDao, Mockito.times(1)).getByParamsLikeIgnoreCase("Название", "123", 1L, 1L);
        Mockito.verify(printService, Mockito.times(1)).printBooks(books);

    }

    @DisplayName("вернёт все книги")
    @Test
    void findAll() {

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = new Genre(1L,"Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres);
        List<Book> books = new ArrayList<>();
        books.add(book);

        given(bookDao.getAll()).willReturn(books);
        given(printService.printBooks(books)).willReturn("");

        bookService.findAll();

        Mockito.verify(bookDao, Mockito.times(1)).getAll();
        Mockito.verify(printService, Mockito.times(1)).printBooks(books);

    }

    @DisplayName("вернёт количество книг")
    @Test
    void count() {

        given(bookDao.count()).willReturn(0);
        given(printService.printBooksCount(0)).willReturn("");

        bookService.count();

        Mockito.verify(bookDao, Mockito.times(1)).count();
        Mockito.verify(printService, Mockito.times(1)).printBooksCount(0);

    }

    @DisplayName("получит уникальные идентификаторы")
    @Test
    void getUniqueIds() {

        List<String> ids = bookService.getUniqueIds("1,2");

        assertThat(ids).hasSize(2);
        assertThat(ids.get(0)).containsIgnoringCase("1");
        assertThat(ids.get(1)).containsIgnoringCase("2");

    }

    @DisplayName("получит уникальные идентификаторы без учёта пробелов")
    @Test
    void getUniqueIdsIgnoreSpaces() {

        List<String> ids = bookService.getUniqueIds(" 1 , 2 ");

        assertThat(ids).hasSize(2);
        assertThat(ids.get(0)).containsIgnoringCase("1");
        assertThat(ids.get(1)).containsIgnoringCase("2");

    }

    @DisplayName("получит уникальные идентификаторы без учёта пробелов")
    @Test
    void getUniqueIdsIgnoreDuplication() {

        List<String> ids = bookService.getUniqueIds("1,2,2,1");

        assertThat(ids).hasSize(2);
        assertThat(ids.get(0)).containsIgnoringCase("1");
        assertThat(ids.get(1)).containsIgnoringCase("2");

    }

    @DisplayName("заполнит авторов и жанры для переданной книге по входным данным")
    @Test
    void processLinks() {

        Author author_1 = new Author(1L,"Фамилия", "Имя", "Отчество");
        Author author_2 = new Author(2L,"Last", "First", "Middle");
        List<Author> authors = new ArrayList<>();
        authors.add(author_1);
        authors.add(author_2);

        Genre genre_1 = new Genre(1L,"Жанр", "Описание");
        Genre genre_2 = new Genre(2L,"Genre", "Description");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre_1);
        genres.add(genre_2);

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>());

        given(authorDao.getByIds(List.of("1", "2"))).willReturn(authors);
        given(genreDao.getByIds(List.of("1", "2"))).willReturn(genres);

        book = bookService.processLinks(book, "1,2", "1,2");

        assertThat(book.getAuthors()).hasSize(2);
        assertThat(book.getAuthors().get(0).getId()).isEqualTo(1L);
        assertThat(book.getAuthors().get(0).getLastName()).isEqualTo("Фамилия");
        assertThat(book.getAuthors().get(0).getFirstName()).isEqualTo("Имя");
        assertThat(book.getAuthors().get(0).getMiddleName()).isEqualTo("Отчество");
        assertThat(book.getAuthors().get(1).getId()).isEqualTo(2L);
        assertThat(book.getAuthors().get(1).getLastName()).isEqualTo("Last");
        assertThat(book.getAuthors().get(1).getFirstName()).isEqualTo("First");
        assertThat(book.getAuthors().get(1).getMiddleName()).isEqualTo("Middle");

        assertThat(book.getGenres()).hasSize(2);
        assertThat(book.getGenres().get(0).getId()).isEqualTo(1L);
        assertThat(book.getGenres().get(0).getName()).containsIgnoringCase("Жанр");
        assertThat(book.getGenres().get(0).getDescription()).containsIgnoringCase("Описание");
        assertThat(book.getGenres().get(1).getId()).isEqualTo(2L);
        assertThat(book.getGenres().get(1).getName()).containsIgnoringCase("Genre");
        assertThat(book.getGenres().get(1).getDescription()).containsIgnoringCase("Description");

    }

    @DisplayName("не заполнит авторов и жанры для null значений авторов и значений")
    @Test
    void processLinksNullAuthorsAndGenres() {

        Author author_1 = new Author(1L,"Фамилия", "Имя", "Отчество");
        Author author_2 = new Author(2L,"Last", "First", "Middle");
        List<Author> authors = new ArrayList<>();
        authors.add(author_1);
        authors.add(author_2);

        Genre genre_1 = new Genre(1L,"Жанр", "Описание");
        Genre genre_2 = new Genre(2L,"Genre", "Description");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre_1);
        genres.add(genre_2);

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>());

        book = bookService.processLinks(book, null, null);

        assertThat(book.getAuthors()).hasSize(0);
        assertThat(book.getGenres()).hasSize(0);

    }

    @DisplayName("не заполнит авторов и жанры для пустых значений авторов и значений")
    @Test
    void processLinksEmptyAuthorsAndGenres() {

        Author author_1 = new Author(1L,"Фамилия", "Имя", "Отчество");
        Author author_2 = new Author(2L,"Last", "First", "Middle");
        List<Author> authors = new ArrayList<>();
        authors.add(author_1);
        authors.add(author_2);

        Genre genre_1 = new Genre(1L,"Жанр", "Описание");
        Genre genre_2 = new Genre(2L,"Genre", "Description");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre_1);
        genres.add(genre_2);

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>());

        book = bookService.processLinks(book, "", "");

        assertThat(book.getAuthors()).hasSize(0);
        assertThat(book.getGenres()).hasSize(0);

    }

    @DisplayName("не заполнит авторов и жанры для пустых значений авторов и значений без учёта пробелов")
    @Test
    void processLinksEmptyAuthorsAndGenresIgnoreSpaces() {

        Author author_1 = new Author(1L,"Фамилия", "Имя", "Отчество");
        Author author_2 = new Author(2L,"Last", "First", "Middle");
        List<Author> authors = new ArrayList<>();
        authors.add(author_1);
        authors.add(author_2);

        Genre genre_1 = new Genre(1L,"Жанр", "Описание");
        Genre genre_2 = new Genre(2L,"Genre", "Description");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre_1);
        genres.add(genre_2);

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>());

        book = bookService.processLinks(book, "  ", "  ");

        assertThat(book.getAuthors()).hasSize(0);
        assertThat(book.getGenres()).hasSize(0);

    }

    @DisplayName("не заполнит авторов при несуществующих идентификаторах")
    @Test
    void processLinksInvalidAuthors() {

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>());

        given(authorDao.getByIds(List.of("1"))).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.processLinks(book, "1", null);

        });

    }

    @DisplayName("не заполнит жанры при несуществующих идентификаторах")
    @Test
    void processLinksInvalidGenres() {

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>());

        given(genreDao.getByIds(List.of("1"))).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.processLinks(book, null, "1");

        });

    }

}
