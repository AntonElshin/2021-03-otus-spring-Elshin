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
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.QBook;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с книгами должен")
@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PrintBookService printBookService;

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

        Book book = new Book("Название", "123", "Описание", authors, genres, null);
        Book createdBook = new Book(1L, "Название", "123", "Описание", authors, genres, null);

        BooleanExpression predicate = QBook.book.isbn.equalsIgnoreCase("123");

        given(bookRepository.findAll(predicate)).willReturn(new ArrayList<>());
        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(book)).willReturn(createdBook);

        bookService.add("Название", "123", "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(predicate);
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book("Название", null, "Описание", authors, genres, null);
        Book createdBook = new Book(1L, "Название", null, "Описание", authors, genres, null);

        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(book)).willReturn(createdBook);

        bookService.add("Название", null, "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(0)).findAll(Mockito.any(BooleanExpression.class));
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book("Название", "", "Описание", authors, genres, null);

        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(book)).willReturn(book);

        bookService.add("Название", "", "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(0)).findAll(Mockito.any(BooleanExpression.class));
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book("Название", "  ", "Описание", authors, genres, null);
        Book createdBook = new Book("Название", "  ", "Описание", authors, genres, null);

        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(book)).willReturn(createdBook);

        bookService.add("Название", "  ", "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(0)).findAll(Mockito.any(BooleanExpression.class));
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book("Название", "123", "Описание", new ArrayList<>(), new ArrayList<>(), null);
        List<Book> books = new ArrayList<>();
        books.add(book);

        BooleanExpression predicate = QBook.book.isbn.equalsIgnoreCase("123");

        given(bookRepository.findAll(predicate)).willReturn(books);

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

        BooleanExpression predicate = QBook.book.isbn.equalsIgnoreCase("123");

        given(bookRepository.findAll(predicate)).willReturn(new ArrayList<>());
        given(authorRepository.findByIds(List.of(3L))).willReturn(null);

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

        BooleanExpression predicate = QBook.book.isbn.equalsIgnoreCase("123");

        given(bookRepository.findAll(predicate)).willReturn(new ArrayList<>());
        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(3L))).willReturn(null);

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

        Book book = new Book(1L,"Название", null, "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(book)).willReturn(book);

        bookService.update(1L, "Название", null, "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(0)).findAll(Mockito.any(BooleanExpression.class));
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book(1L,"Название", "", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(book)).willReturn(book);

        bookService.update(1L, "Название", "", "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(0)).findAll(Mockito.any(BooleanExpression.class));
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book(1L,"Название", "  ", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(book)).willReturn(book);

        bookService.update(1L, "Название", "  ", "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(0)).findAll(Mockito.any(BooleanExpression.class));
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book(1L,"Название", "123", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(book)).willReturn(book);

        BooleanExpression predicate = QBook.book.isbn.containsIgnoreCase("123");

        bookService.update(1L, "Название", "123", "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(0)).findAll(predicate);
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book foundBook = new Book(1L, "Название", "123", "Описание", authors, genres, null);
        Book updateBook = new Book(1L, "Название", "1234", "Описание", authors, genres, null);
        List<Book> books = new ArrayList<>();
        books.add(foundBook);

        BooleanExpression predicate = QBook.book.isbn.equalsIgnoreCase("1234");

        given(bookRepository.findById(1L)).willReturn(Optional.of(foundBook));
        given(bookRepository.findAll(predicate)).willReturn(new ArrayList<>());
        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L))).willReturn(genres);
        given(bookRepository.save(updateBook)).willReturn(updateBook);

        bookService.update(1L, "Название", "1234", "Описание", "1", "1");

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(predicate);
        Mockito.verify(authorRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(genreRepository, Mockito.times(1)).findByIds(List.of(1L));
        Mockito.verify(bookRepository, Mockito.times(1)).save(updateBook);

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

        given(bookRepository.findById(1L)).willReturn(Optional.empty());

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

        Book foundBook = new Book(1L, "Название", "123", "Описание", authors, genres, null);
        Book book = new Book(1L, "Название", "1234", "Описание", authors, genres, null);

        BooleanExpression predicate = QBook.book.isbn.equalsIgnoreCase("1234");

        given(bookRepository.findById(1L)).willReturn(Optional.of(foundBook));
        given(bookRepository.findAll(predicate)).willReturn(List.of(foundBook));

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

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        bookService.update(1L, null, null, null, null, null);

        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        bookService.update(1L, "", "", "", "", "");

        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        bookService.update(1L, "  ", "  ", "  ", "  ", "  ");

        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

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

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(authorRepository.findByIds(List.of(2L))).willReturn(null);

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

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(authorRepository.findByIds(List.of(1L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(2L))).willReturn(null);

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

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres, null);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(printBookService.printBook(book)).willReturn("");

        bookService.getById(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(printBookService, Mockito.times(1)).printBook(book);

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

        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.getById(1L);

        });

    }

    @DisplayName("удалить книгу по переданному идентификатору")
    @Test
    void deleteById() {

        Book book = new Book(1L, "Название", "ISBN", "Описание", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        Mockito.doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteById(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);

    }

    @DisplayName("не удалить книгу из-за нулевого идентификатора")
    @Test
    void deleteByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.deleteById(0);

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

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres, null);
        List<Book> books = new ArrayList<>();
        books.add(book);

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("Название")
                .and(QBook.book.isbn.containsIgnoreCase("123"))
                .and(QBook.book.authors.any().id.eq(1L))
                .and(QBook.book.genres.any().id.eq(1L))
                ;

        given(bookRepository.findAll(predicate)).willReturn(books);
        given(printBookService.printBooks(books)).willReturn("");

        bookService.findByParams("Название", "123", 1L, 1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(predicate);
        Mockito.verify(printBookService, Mockito.times(1)).printBooks(books);

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

        Book book = new Book(1L, "Название", "123", "Описание", authors, genres, null);
        List<Book> books = new ArrayList<>();
        books.add(book);

        given(bookRepository.findAll()).willReturn(books);
        given(printBookService.printBooks(books)).willReturn("");

        bookService.findAll();

        Mockito.verify(bookRepository, Mockito.times(1)).findAll();
        Mockito.verify(printBookService, Mockito.times(1)).printBooks(books);

    }

    @DisplayName("вернёт количество книг")
    @Test
    void count() {

        given(bookRepository.count()).willReturn(0L);
        given(printBookService.printBooksCount(0L)).willReturn("");

        bookService.count();

        Mockito.verify(bookRepository, Mockito.times(1)).count();
        Mockito.verify(printBookService, Mockito.times(1)).printBooksCount(0L);

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

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>(), null);

        given(authorRepository.findByIds(List.of(1L, 2L))).willReturn(authors);
        given(genreRepository.findByIds(List.of(1L, 2L))).willReturn(genres);

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

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>(), null);

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

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>(), null);

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

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>(), null);

        book = bookService.processLinks(book, "  ", "  ");

        assertThat(book.getAuthors()).hasSize(0);
        assertThat(book.getGenres()).hasSize(0);

    }

    @DisplayName("не заполнит авторов при несуществующих идентификаторах")
    @Test
    void processLinksInvalidAuthors() {

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>(), null);

        given(authorRepository.findByIds(List.of(1L))).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.processLinks(book, "1", null);

        });

    }

    @DisplayName("не заполнит жанры при несуществующих идентификаторах")
    @Test
    void processLinksInvalidGenres() {

        Book book = new Book(1L, "Название", "123", "Описание", new ArrayList<>(), new ArrayList<>(), null);

        given(genreRepository.findByIds(List.of(1L))).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            bookService.processLinks(book, null, "1");

        });

    }

}
