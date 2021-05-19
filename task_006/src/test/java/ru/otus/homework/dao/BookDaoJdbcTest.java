package ru.otus.homework.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.otus.homework.dao.ext.BookAuthor;
import ru.otus.homework.dao.ext.BookGenre;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с книгами должно")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
public class BookDaoJdbcTest {

    private static final int EXPECTED_BOOKS_COUNT = 4;

    private static final int EXISTING_BOOK_ID = 2;
    private static final String EXISTING_BOOK_NAME = "Повесть о настоящем человеке";
    private static final String EXISTING_BOOK_ISBN = "978-5-17-064314-1";
    private static final String EXISTING_BOOK_DESCRIPTION = "«Повесть о настоящем человеке» рассказывает о подвиге летчика, сбитого в бою во время Великой Отечественной войны. Раненый, обмороженный, он сумел добраться до своих, а после ампутации ног вернулся в строй.";

    private static final int EXISTING_GENRE_ID = 2;
    private static final String EXISTING_GENRE_NAME = "Повесть";
    private static final String EXISTING_GENRE_DESCRIPTION = "Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя";

    private static final int EXISTING_AUTHOR_ID = 2;
    private static final String EXISTING_AUTHOR_LAST_NAME = "Полевой";
    private static final String EXISTING_AUTHOR_FIRST_NAME = "Борис";
    private static final String EXISTING_AUTHOR_MIDDLE_NAME = "Николаевич";

    @Autowired
    private BookDaoJdbc bookDao;

    @Autowired
    private AuthorDaoJdbc authorDao;

    @Autowired
    private GenreDaoJdbc genreDao;

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @DisplayName("возвращать ожидаемое количество жанров")
    @Test
    void shouldReturnExpectedBookCount() {
        int actualBooksCount = bookDao.count();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldInsertBook() {
        Book expectedBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>());
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, expectedBook);
        expectedBook.setId(bookId);

        Book actualBook = bookDao.getById(bookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("добавлять книгу в БД с авторами")
    @Test
    void shouldInsertBookWithAuthors() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        long authorId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_1, author_1);
        author_1.setId(authorId_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        long authorId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_2, author_2);
        author_2.setId(authorId_2);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", createdAuthors, new ArrayList<>());
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, expectedBook);
        expectedBook.setId(bookId);

        Book actualBook = bookDao.getById(bookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("добавлять книгу в БД с жанрами")
    @Test
    void shouldInsertBookWithGenres() {

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        long genreId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_1, genre_1);
        genre_1.setId(genreId_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        long genreId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_2, genre_2);
        genre_2.setId(genreId_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), createdGenres);
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, expectedBook);
        expectedBook.setId(bookId);

        Book actualBook = bookDao.getById(bookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("добавлять книгу в БД с авторами и жанрами")
    @Test
    void shouldInsertBookWithAuthorsAndGenres() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        long authorId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_1, author_1);
        author_1.setId(authorId_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        long authorId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_2, author_2);
        author_2.setId(authorId_2);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        long genreId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_1, genre_1);
        genre_1.setId(genreId_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        long genreId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_2, genre_2);
        genre_2.setId(genreId_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", createdAuthors, createdGenres);
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, expectedBook);
        expectedBook.setId(bookId);

        Book actualBook = bookDao.getById(bookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("изменять книгу в БД")
    @Test
    void shouldUpdateBook() {
        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>());
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, createdBook);
        createdBook.setId(bookId);

        Book expectedBook = new Book("Название книги 1", "1234", "Описание 1", new ArrayList<>(), new ArrayList<>());
        bookDao.update(bookId, expectedBook);
        expectedBook.setId(bookId);

        Book actualBook = bookDao.getById(bookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("изменять книгу в БД с авторами")
    @Test
    void shouldUpdateBookWithAuthors() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        long authorId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_1, author_1);
        author_1.setId(authorId_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        long authorId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_2, author_2);
        author_2.setId(authorId_2);

        Author author_3 = new Author("LastName 3", "FirstName 3", "MiddleName 3");
        long authorId_3 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_3, author_3);
        author_3.setId(authorId_3);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Book createdBook = new Book("Название книги", "123", "Описание", createdAuthors, new ArrayList<>());
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, createdBook);
        createdBook.setId(bookId);

        List<Author> updatedAuthors = new ArrayList<>();
        updatedAuthors.add(author_3);

        Book expectedBook = new Book("Название книги 1", "1234", "Описание 1", updatedAuthors, new ArrayList<>());
        bookDao.update(bookId, expectedBook);
        expectedBook.setId(bookId);

        Book actualBook = bookDao.getById(bookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("изменять книгу в БД с жанрами")
    @Test
    void shouldUpdateBookWithGenres() {

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        long genreId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_1, genre_1);
        genre_1.setId(genreId_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        long genreId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_2, genre_2);
        genre_2.setId(genreId_2);

        Genre genre_3 = new Genre("Жанр 3", "Описание 3");
        long genreId_3 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_3, genre_3);
        genre_3.setId(genreId_3);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), createdGenres);
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, createdBook);
        createdBook.setId(bookId);

        List<Genre> updateGenres = new ArrayList<>();
        updateGenres.add(genre_3);

        Book expectedBook = new Book("Название книги 1", "1234", "Описание 1", new ArrayList<>(), updateGenres);
        bookDao.update(bookId, expectedBook);
        expectedBook.setId(bookId);

        Book actualBook = bookDao.getById(bookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("изменять книгу в БД с авторами и жанрами")
    @Test
    void shouldUpdateBookWithAuthorsAndGenres() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        long authorId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_1, author_1);
        author_1.setId(authorId_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        long authorId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_2, author_2);
        author_2.setId(authorId_2);

        Author author_3 = new Author("LastName 3", "FirstName 3", "MiddleName 3");
        long authorId_3 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_3, author_3);
        author_3.setId(authorId_3);

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        long genreId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_1, genre_1);
        genre_1.setId(genreId_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        long genreId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_2, genre_2);
        genre_2.setId(genreId_2);

        Genre genre_3 = new Genre("Жанр 3", "Описание 3");
        long genreId_3 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_3, genre_3);
        genre_3.setId(genreId_3);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book createdBook = new Book("Название книги", "123", "Описание", createdAuthors, createdGenres);
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, createdBook);
        createdBook.setId(bookId);

        List<Author> updatedAuthors = new ArrayList<>();
        updatedAuthors.add(author_3);

        List<Genre> updateGenres = new ArrayList<>();
        updateGenres.add(genre_3);

        Book expectedBook = new Book("Название книги 1", "1234", "Описание 1", updatedAuthors, updateGenres);
        bookDao.update(bookId, expectedBook);
        expectedBook.setId(bookId);

        Book actualBook = bookDao.getById(bookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {

        Author author = new Author(EXISTING_AUTHOR_LAST_NAME, EXISTING_AUTHOR_FIRST_NAME, EXISTING_AUTHOR_MIDDLE_NAME);
        author.setId(EXISTING_AUTHOR_ID);

        Genre genre = new Genre(EXISTING_GENRE_NAME, EXISTING_GENRE_DESCRIPTION);
        genre.setId(EXISTING_GENRE_ID);

        Book expectedBook = new Book(EXISTING_BOOK_NAME, EXISTING_BOOK_ISBN, EXISTING_BOOK_DESCRIPTION, List.of(author), List.of(genre));
        expectedBook.setId(EXISTING_BOOK_ID);
        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("удалять заданную книгу по её id")
    @Test
    void shouldCorrectDeleteBookById() {

        Book expectedBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>());
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, expectedBook);
        expectedBook.setId(bookId);

        assertThatCode(() -> bookDao.getById(bookId))
                .doesNotThrowAnyException();

        bookDao.deleteById(bookId);

        assertThatThrownBy(() -> bookDao.getById(bookId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("удалять заданную книгу по её id и связи с авторами")
    @Test
    void shouldCorrectDeleteBookByIdWithAuthorLinks() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        long authorId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_1, author_1);
        author_1.setId(authorId_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        long authorId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_2, author_2);
        author_2.setId(authorId_2);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", createdAuthors, new ArrayList<>());
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, expectedBook);
        expectedBook.setId(bookId);

        List<BookAuthor> bookAuthorsBefore = bookDao.getBookAuthors(List.of("" + bookId));
        assertThat(bookAuthorsBefore.size()).isEqualTo(2);

        assertThatCode(() -> bookDao.getById(bookId))
                .doesNotThrowAnyException();

        bookDao.deleteById(bookId);

        assertThatThrownBy(() -> bookDao.getById(bookId))
                .isInstanceOf(EmptyResultDataAccessException.class);

        List<BookAuthor> bookAuthorsAfter = bookDao.getBookAuthors(List.of("" + bookId));
        assertThat(bookAuthorsAfter.size()).isEqualTo(0);

    }

    @DisplayName("удалять заданную книгу по её id и связи с жанрами")
    @Test
    void shouldCorrectDeleteBookByIdWithGenreLinks() {

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        long genreId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_1, genre_1);
        genre_1.setId(genreId_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        long genreId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_2, genre_2);
        genre_2.setId(genreId_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), createdGenres);
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, expectedBook);
        expectedBook.setId(bookId);

        List<BookGenre> bookGenresBefore = bookDao.getBookGenres(List.of("" + bookId));
        assertThat(bookGenresBefore.size()).isEqualTo(2);

        assertThatCode(() -> bookDao.getById(bookId))
                .doesNotThrowAnyException();

        bookDao.deleteById(bookId);

        assertThatThrownBy(() -> bookDao.getById(bookId))
                .isInstanceOf(EmptyResultDataAccessException.class);

        List<BookGenre> bookGenresAfter = bookDao.getBookGenres(List.of("" + bookId));
        assertThat(bookGenresAfter.size()).isEqualTo(0);

    }

    @DisplayName("удалять заданную книгу по её id и связи с авторами и жанрами")
    @Test
    void shouldCorrectDeleteBookByIdWithAuthorAndGenreLinks() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        long authorId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_1, author_1);
        author_1.setId(authorId_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        long authorId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_2, author_2);
        author_2.setId(authorId_2);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        long genreId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_1, genre_1);
        genre_1.setId(genreId_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        long genreId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_2, genre_2);
        genre_2.setId(genreId_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", createdAuthors, createdGenres);
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, expectedBook);
        expectedBook.setId(bookId);

        List<BookAuthor> bookAuthorsBefore = bookDao.getBookAuthors(List.of("" + bookId));
        assertThat(bookAuthorsBefore.size()).isEqualTo(2);

        List<BookGenre> bookGenresBefore = bookDao.getBookGenres(List.of("" + bookId));
        assertThat(bookGenresBefore.size()).isEqualTo(2);

        assertThatCode(() -> bookDao.getById(bookId))
                .doesNotThrowAnyException();

        bookDao.deleteById(bookId);

        assertThatThrownBy(() -> bookDao.getById(bookId))
                .isInstanceOf(EmptyResultDataAccessException.class);

        List<BookAuthor> bookAuthorsAfter = bookDao.getBookAuthors(List.of("" + bookId));
        assertThat(bookAuthorsAfter.size()).isEqualTo(0);

        List<BookGenre> bookGenresAfter = bookDao.getBookGenres(List.of("" + bookId));
        assertThat(bookGenresAfter.size()).isEqualTo(0);

    }

    @DisplayName("вернуть все книги")
    @Test
    void shouldFindAllBooks() {

        List<Book> books = bookDao.getAll();

        assertThat(books).hasSize(4);

        assertThat(books.get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Весь Шерлок Холмс");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-17-105207-2");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("Произведения, на которых мы выросли, – и произведения, совершенно нам незнакомые. Все, что написал о великом сыщике с Бейкер-стрит сам сэр Артур Конан Дойл – от классических повестей до небольших рассказов.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Конан");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(books.get(1).getId()).isEqualTo(2L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Повесть о настоящем человеке");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-17-064314-1");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("«Повесть о настоящем человеке» рассказывает о подвиге летчика, сбитого в бою во время Великой Отечественной войны. Раненый, обмороженный, он сумел добраться до своих, а после ампутации ног вернулся в строй.");
        assertThat(books.get(1).getAuthors()).hasSize(1);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(2L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Николаевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(2L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Повесть");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

        assertThat(books.get(2).getId()).isEqualTo(3L);
        assertThat(books.get(2).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(2).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(2).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(2).getAuthors()).hasSize(1);
        assertThat(books.get(2).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(2).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(2).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(2).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(2).getGenres()).hasSize(1);
        assertThat(books.get(2).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(2).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(2).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(3).getId()).isEqualTo(4L);
        assertThat(books.get(3).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(3).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(3).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(3).getAuthors()).hasSize(3);
        assertThat(books.get(3).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(3).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(3).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(3).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(3).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(3).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(3).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(3).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(3).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(3).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(3).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(3).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(3).getGenres()).hasSize(1);
        assertThat(books.get(3).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(3).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(3).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");
    }

    @DisplayName("найти все книги при поиске без параметров (like)")
    @Test
    void shouldFindAllBooksLike() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase(null, null, null, null);

        assertThat(books).hasSize(4);

        assertThat(books.get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Весь Шерлок Холмс");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-17-105207-2");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("Произведения, на которых мы выросли, – и произведения, совершенно нам незнакомые. Все, что написал о великом сыщике с Бейкер-стрит сам сэр Артур Конан Дойл – от классических повестей до небольших рассказов.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Конан");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(books.get(1).getId()).isEqualTo(2L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Повесть о настоящем человеке");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-17-064314-1");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("«Повесть о настоящем человеке» рассказывает о подвиге летчика, сбитого в бою во время Великой Отечественной войны. Раненый, обмороженный, он сумел добраться до своих, а после ампутации ног вернулся в строй.");
        assertThat(books.get(1).getAuthors()).hasSize(1);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(2L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Николаевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(2L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Повесть");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

        assertThat(books.get(2).getId()).isEqualTo(3L);
        assertThat(books.get(2).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(2).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(2).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(2).getAuthors()).hasSize(1);
        assertThat(books.get(2).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(2).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(2).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(2).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(2).getGenres()).hasSize(1);
        assertThat(books.get(2).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(2).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(2).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(3).getId()).isEqualTo(4L);
        assertThat(books.get(3).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(3).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(3).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(3).getAuthors()).hasSize(3);
        assertThat(books.get(3).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(3).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(3).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(3).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(3).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(3).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(3).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(3).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(3).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(3).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(3).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(3).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(3).getGenres()).hasSize(1);
        assertThat(books.get(3).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(3).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(3).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части названия")
    @Test
    void shouldFindAllBooksLikePartTitle() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase("ы", null, null, null);

        assertThat(books).hasSize(2);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(1).getAuthors()).hasSize(3);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(1).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(1).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(1).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(1).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(1).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(1).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(1).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части ISBN")
    @Test
    void shouldFindAllBooksLikePartIsbn() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase(null, "353", null, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов (like) по идентификатору автора")
    @Test
    void shouldFindAllBooksLikeAuthorId() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase(null, null, 3L, null);

        assertThat(books).hasSize(2);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(1).getAuthors()).hasSize(3);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(1).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(1).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(1).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(1).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(1).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(1).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(1).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов (like) по идентификатору жанра")
    @Test
    void shouldFindAllBooksLikeGenreId() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase(null, null, null, 3L);

        assertThat(books).hasSize(2);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(1).getAuthors()).hasSize(3);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(1).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(1).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(1).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(1).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(1).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(1).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(1).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части названия и ISBN")
    @Test
    void shouldFindAllBooksLikePartTitleAndIsbn() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase("ы", "353", null, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части названия, ISBN, идентификатору автора")
    @Test
    void shouldFindAllBooksLikePartTitleAndIsbnAndAuthorId() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase("ы", "353", 3L, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части названия, ISBN, идентификатору автора и жанра")
    @Test
    void shouldFindAllBooksLikePartTitleAndIsbnAndAuthorIdAndGenreId() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase("ы", "353", 3L, 3L);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части названия без учёта регистра")
    @Test
    void shouldFindAllBooksLikePartTitleIgnoreCase() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase("ы", null, null, null);

        assertThat(books).hasSize(2);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(1).getAuthors()).hasSize(3);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(1).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(1).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(1).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(1).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(1).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(1).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(1).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части названия и ISBN без учёта регистра")
    @Test
    void shouldFindAllBooksLikePartTitleAndIsbnIgnoreCase() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase("Ы", "353", null, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части названия, ISBN, идентификатору автора без учёта регистра")
    @Test
    void shouldFindAllBooksLikePartTitleAndIsbnAndAuthorIdIgnoreCase() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase("Ы", "353", 3L, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по части названия, ISBN, идентификатору автора и жанра без учёта регистра")
    @Test
    void shouldFindAllBooksLikePartTitleAndIsbnAndAuthorIdAndGenreIdIgnoreCase() {

        List<Book> books = bookDao.getByParamsLikeIgnoreCase("Ы", "353", 3L, 3L);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти все книги при поиске без параметров (равенство)")
    @Test
    void shouldFindAllBooksEquals() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase(null, null, null, null);

        assertThat(books).hasSize(4);

        assertThat(books.get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Весь Шерлок Холмс");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-17-105207-2");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("Произведения, на которых мы выросли, – и произведения, совершенно нам незнакомые. Все, что написал о великом сыщике с Бейкер-стрит сам сэр Артур Конан Дойл – от классических повестей до небольших рассказов.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Конан");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(1L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(books.get(1).getId()).isEqualTo(2L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Повесть о настоящем человеке");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-17-064314-1");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("«Повесть о настоящем человеке» рассказывает о подвиге летчика, сбитого в бою во время Великой Отечественной войны. Раненый, обмороженный, он сумел добраться до своих, а после ампутации ног вернулся в строй.");
        assertThat(books.get(1).getAuthors()).hasSize(1);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(2L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Николаевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(2L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Повесть");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

        assertThat(books.get(2).getId()).isEqualTo(3L);
        assertThat(books.get(2).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(2).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(2).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(2).getAuthors()).hasSize(1);
        assertThat(books.get(2).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(2).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(2).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(2).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(2).getGenres()).hasSize(1);
        assertThat(books.get(2).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(2).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(2).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(3).getId()).isEqualTo(4L);
        assertThat(books.get(3).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(3).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(3).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(3).getAuthors()).hasSize(3);
        assertThat(books.get(3).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(3).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(3).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(3).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(3).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(3).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(3).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(3).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(3).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(3).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(3).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(3).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(3).getGenres()).hasSize(1);
        assertThat(books.get(3).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(3).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(3).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству названия")
    @Test
    void shouldFindAllBooksEqualsTitle() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase("Сказка о рыбаке и рыбке", null, null, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству ISBN")
    @Test
    void shouldFindAllBooksEqualsIsbn() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase(null, "978-5-353-08602-4", null, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов (равенство) по идентификатору автора")
    @Test
    void shouldFindAllBooksEqualsAuthorId() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase(null, null, 3L, null);

        assertThat(books).hasSize(2);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(1).getAuthors()).hasSize(3);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(1).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(1).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(1).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(1).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(1).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(1).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(1).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов (равенство) по идентификатору жанра")
    @Test
    void shouldFindAllBooksEqualsGenreId() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase(null, null, null, 3L);

        assertThat(books).hasSize(2);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

        assertThat(books.get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getTitle()).containsIgnoringCase("Русские волшебные сказки");
        assertThat(books.get(1).getIsbn()).containsIgnoringCase("978-5-00108-639-0");
        assertThat(books.get(1).getDescription()).containsIgnoringCase("В сборник вошли известные и любимые всеми сказки: «Аленький цветочек» С. Т. Аксакова, «Спящая царевна» В. А. Жуковского, «Сказка о рыбаке и рыбке» А. С. Пушкина. Иллюстрации Бориса Александровича Дехтерёва обладают своим особым, неповторимым шармом и прекрасно дополняют волшебный мир, созданный русскими писателями. Художник не просто нарисовал изящные картины, он в присущем лишь ему одному стиле передал объём и движение при помощи цвета и тени.");
        assertThat(books.get(1).getAuthors()).hasSize(3);
        assertThat(books.get(1).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(1).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(1).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(1).getAuthors().get(1).getId()).isEqualTo(4L);
        assertThat(books.get(1).getAuthors().get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(books.get(1).getAuthors().get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(books.get(1).getAuthors().get(1).getMiddleName()).containsIgnoringCase("Андреевич");
        assertThat(books.get(1).getAuthors().get(2).getId()).isEqualTo(5L);
        assertThat(books.get(1).getAuthors().get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(books.get(1).getAuthors().get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(books.get(1).getAuthors().get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");
        assertThat(books.get(1).getGenres()).hasSize(1);
        assertThat(books.get(1).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(1).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(1).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству названия и ISBN")
    @Test
    void shouldFindAllBooksEqualsTitleAndIsbn() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase("Сказка о рыбаке и рыбке", "978-5-353-08602-4", null, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству названия, ISBN, идентификатору автора")
    @Test
    void shouldFindAllBooksEqualsTitleAndIsbnAndAuthorId() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase("Сказка о рыбаке и рыбке", "978-5-353-08602-4", 3L, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству названия, ISBN, идентификатору автора и жанра")
    @Test
    void shouldFindAllBooksEqualsTitleAndIsbnAndAuthorIdAndGenreId() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase("СКАЗКА о рыбаке и рыбке", "978-5-353-08602-4", 3L, 3L);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству названия без учёта регистра")
    @Test
    void shouldFindAllBooksEqualsTitleIgnoreCase() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase("СКАЗКА о рыбаке и рыбке", null, null, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству названия и ISBN без учёта регистра")
    @Test
    void shouldFindAllBooksEqualsTitleAndIsbnIgnoreCase() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase("СКАЗКА о рыбаке и рыбке", "978-5-353-08602-4", null, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству названия, ISBN, идентификатору автора без учёта регистра")
    @Test
    void shouldFindAllBooksEqualsTitleAndIsbnAndAuthorIdIgnoreCase() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase("СКАЗКА о рыбаке и рыбке", "978-5-353-08602-4", 3L, null);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти всех авторов по равенству названия, ISBN, идентификатору автора и жанра без учёта регистра")
    @Test
    void shouldFindAllBooksEqualsTitleAndIsbnAndAuthorIdAndGenreIdIgnoreCase() {

        List<Book> books = bookDao.getByParamsEqualsIgnoreCase("СКАЗКА о рыбаке и рыбке", "978-5-353-08602-4", 3L, 3L);

        assertThat(books).hasSize(1);

        assertThat(books.get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Сказка о рыбаке и рыбке");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("978-5-353-08602-4");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("А. С. Пушкин писал свои сказки не для детей, однако они давно стали классикой именно детской литературы. Уникальность этого издания «Сказки о рыбаке и рыбке» состоит в том, что оно подготовлено с учетом особенностей детского восприятия: текст разбит на небольшие фрагменты и каждый из них проиллюстрирован. Это помогает малышу не только лучше воспринимать услышанное, усваивать, запоминать, но и делает совместное чтение интересным и занимательным.");
        assertThat(books.get(0).getAuthors()).hasSize(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("Александр");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("Сергеевич");
        assertThat(books.get(0).getGenres()).hasSize(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(3L);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Сказка");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("получить связи книги с авторами и жанрами")
    @Test
    void shouldGetBookLinksWithAuthorsAndGenres() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        long authorId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_1, author_1);
        author_1.setId(authorId_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        long authorId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_2, author_2);
        author_2.setId(authorId_2);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        long genreId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_1, genre_1);
        genre_1.setId(genreId_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        long genreId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_2, genre_2);
        genre_2.setId(genreId_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book createdBook = new Book("Название книги", "123", "Описание", createdAuthors, createdGenres);
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, createdBook);
        createdBook.setId(bookId);

        createdBook.setAuthors(new ArrayList<>());
        createdBook.setGenres(new ArrayList<>());

        List<Book> books = bookDao.getBooksLinks(List.of(createdBook));

        assertThat(books.get(0).getId()).isEqualTo(bookId);
        assertThat(books.get(0).getTitle()).containsIgnoringCase("Название книги");
        assertThat(books.get(0).getIsbn()).containsIgnoringCase("123");
        assertThat(books.get(0).getDescription()).containsIgnoringCase("Описание");
        assertThat(books.get(0).getAuthors()).hasSize(2);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(authorId_1);
        assertThat(books.get(0).getAuthors().get(0).getLastName()).containsIgnoringCase("LastName 1");
        assertThat(books.get(0).getAuthors().get(0).getFirstName()).containsIgnoringCase("FirstName 1");
        assertThat(books.get(0).getAuthors().get(0).getMiddleName()).containsIgnoringCase("MiddleName 1");
        assertThat(books.get(0).getAuthors().get(1).getId()).isEqualTo(authorId_2);
        assertThat(books.get(0).getAuthors().get(1).getLastName()).containsIgnoringCase("LastName 2");
        assertThat(books.get(0).getAuthors().get(1).getFirstName()).containsIgnoringCase("FirstName 2");
        assertThat(books.get(0).getAuthors().get(1).getMiddleName()).containsIgnoringCase("MiddleName 2");
        assertThat(books.get(0).getGenres()).hasSize(2);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(genreId_1);
        assertThat(books.get(0).getGenres().get(0).getName()).containsIgnoringCase("Жанр 1");
        assertThat(books.get(0).getGenres().get(0).getDescription()).containsIgnoringCase("Описание 1");
        assertThat(books.get(0).getGenres().get(1).getId()).isEqualTo(genreId_2);
        assertThat(books.get(0).getGenres().get(1).getName()).containsIgnoringCase("Жанр 2");
        assertThat(books.get(0).getGenres().get(1).getDescription()).containsIgnoringCase("Описание 2");

    }

    @DisplayName("получить связи книги с авторами")
    @Test
    void shouldGetBookLinksWithAuthors() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        long authorId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_1, author_1);
        author_1.setId(authorId_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        long authorId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId_2, author_2);
        author_2.setId(authorId_2);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Book createdBook = new Book("Название книги", "123", "Описание", createdAuthors, new ArrayList<>());
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, createdBook);
        createdBook.setId(bookId);

        createdBook.setAuthors(new ArrayList<>());

        List<BookAuthor> bookAuthors = bookDao.getBookAuthors(List.of("" + bookId));

        assertThat(bookAuthors.get(0).getBookId()).isEqualTo(bookId);
        assertThat(bookAuthors.get(0).getAuthor().getId()).isEqualTo(authorId_1);
        assertThat(bookAuthors.get(0).getAuthor().getLastName()).containsIgnoringCase("LastName 1");
        assertThat(bookAuthors.get(0).getAuthor().getFirstName()).containsIgnoringCase("FirstName 1");
        assertThat(bookAuthors.get(0).getAuthor().getMiddleName()).containsIgnoringCase("MiddleName 1");
        assertThat(bookAuthors.get(1).getBookId()).isEqualTo(bookId);
        assertThat(bookAuthors.get(1).getAuthor().getId()).isEqualTo(authorId_2);
        assertThat(bookAuthors.get(1).getAuthor().getLastName()).containsIgnoringCase("LastName 2");
        assertThat(bookAuthors.get(1).getAuthor().getFirstName()).containsIgnoringCase("FirstName 2");
        assertThat(bookAuthors.get(1).getAuthor().getMiddleName()).containsIgnoringCase("MiddleName 2");

    }

    @DisplayName("получить связи книги с жанрами")
    @Test
    void shouldGetBookLinksWithGenres() {

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        long genreId_1 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_1, genre_1);
        genre_1.setId(genreId_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        long genreId_2 = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId_2, genre_2);
        genre_2.setId(genreId_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), createdGenres);
        long bookId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        bookDao.insert(bookId, createdBook);
        createdBook.setId(bookId);

        createdBook.setGenres(new ArrayList<>());

        List<BookGenre> bookGenres = bookDao.getBookGenres(List.of("" + bookId));

        assertThat(bookGenres.get(0).getBookId()).isEqualTo(bookId);
        assertThat(bookGenres.get(0).getGenre().getId()).isEqualTo(genreId_1);
        assertThat(bookGenres.get(0).getGenre().getName()).containsIgnoringCase("Жанр 1");
        assertThat(bookGenres.get(0).getGenre().getDescription()).containsIgnoringCase("Описание 1");
        assertThat(bookGenres.get(1).getBookId()).isEqualTo(bookId);
        assertThat(bookGenres.get(1).getGenre().getId()).isEqualTo(genreId_2);
        assertThat(bookGenres.get(1).getGenre().getName()).containsIgnoringCase("Жанр 2");
        assertThat(bookGenres.get(1).getGenre().getDescription()).containsIgnoringCase("Описание 2");

    }

}
