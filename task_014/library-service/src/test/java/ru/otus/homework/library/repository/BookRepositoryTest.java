package ru.otus.homework.library.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.library.domain.Author;
import ru.otus.homework.library.domain.QBook;
import ru.otus.homework.library.domain.Book;
import ru.otus.homework.library.domain.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Dao для работы с книгами должно")
@DataJpaTest
public class BookRepositoryTest {

    private static final int EXPECTED_BOOKS_COUNT = 4;

    private static final Long EXISTING_BOOK_ID = 2L;
    private static final String EXISTING_BOOK_NAME = "Повесть о настоящем человеке";
    private static final String EXISTING_BOOK_ISBN = "978-5-17-064314-1";
    private static final String EXISTING_BOOK_DESCRIPTION = "«Повесть о настоящем человеке» рассказывает о подвиге летчика, сбитого в бою во время Великой Отечественной войны. Раненый, обмороженный, он сумел добраться до своих, а после ампутации ног вернулся в строй.";

    private static final Long EXISTING_GENRE_ID = 2L;
    private static final String EXISTING_GENRE_NAME = "Повесть";
    private static final String EXISTING_GENRE_DESCRIPTION = "Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя";

    private static final Long EXISTING_AUTHOR_ID = 2L;
    private static final String EXISTING_AUTHOR_LAST_NAME = "Полевой";
    private static final String EXISTING_AUTHOR_FIRST_NAME = "Борис";
    private static final String EXISTING_AUTHOR_MIDDLE_NAME = "Николаевич";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("возвращать ожидаемое количество жанров")
    @Test
    void shouldReturnExpectedBookCount() {
        Long actualBooksCount = bookRepository.count();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldInsertBook() {
        Book expectedBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        expectedBook = bookRepository.save(expectedBook);
        long bookId = expectedBook.getId();

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("добавлять книгу в БД с авторами")
    @Test
    void shouldInsertBookWithAuthors() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        author_1 = em.persist(author_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        author_2 = em.persist(author_2);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", createdAuthors, new ArrayList<>(), new ArrayList<>());
        expectedBook = bookRepository.save(expectedBook);
        long bookId = expectedBook.getId();

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("добавлять книгу в БД с жанрами")
    @Test
    void shouldInsertBookWithGenres() {

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        genre_1 = em.persist(genre_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        genre_2 = em.persist(genre_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), createdGenres, new ArrayList<>());
        expectedBook = bookRepository.save(expectedBook);
        long bookId = expectedBook.getId();

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("добавлять книгу в БД с авторами и жанрами")
    @Test
    void shouldInsertBookWithAuthorsAndGenres() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        author_1 = em.persist(author_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        author_2 = em.persist(author_2);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        genre_1 = em.persist(genre_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        genre_2 = em.persist(genre_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book expectedBook = new Book("Название книги", "123", "Описание", createdAuthors, createdGenres, new ArrayList<>());
        expectedBook = bookRepository.save(expectedBook);
        long bookId = expectedBook.getId();

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("изменять книгу в БД")
    @Test
    void shouldUpdateBook() {
        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        long bookId = em.persistAndGetId(createdBook, Long.class);

        Book expectedBook = new Book(bookId, "Название книги 1", "1234", "Описание 1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        expectedBook = bookRepository.save(expectedBook);

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("изменять книгу в БД с авторами")
    @Test
    void shouldUpdateBookWithAuthors() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        author_1 = em.persist(author_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        author_2 = em.persist(author_2);

        Author author_3 = new Author("LastName 3", "FirstName 3", "MiddleName 3");
        author_3 = em.persist(author_3);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        Book createdBook = new Book("Название книги", "123", "Описание", createdAuthors, new ArrayList<>(), new ArrayList<>());
        long bookId = em.persistAndGetId(createdBook, Long.class);

        List<Author> updatedAuthors = new ArrayList<>();
        updatedAuthors.add(author_3);

        Book expectedBook = new Book(bookId, "Название книги 1", "1234", "Описание 1", updatedAuthors, new ArrayList<>(), new ArrayList<>());
        expectedBook = bookRepository.save(expectedBook);

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("изменять книгу в БД с жанрами")
    @Test
    void shouldUpdateBookWithGenres() {

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        genre_1 = em.persist(genre_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        genre_2 = em.persist(genre_2);

        Genre genre_3 = new Genre("Жанр 3", "Описание 3");
        genre_3 = em.persist(genre_3);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book createdBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), createdGenres, new ArrayList<>());
        long bookId = em.persistAndGetId(createdBook, Long.class);

        List<Genre> updateGenres = new ArrayList<>();
        updateGenres.add(genre_3);

        Book expectedBook = new Book(bookId, "Название книги 1", "1234", "Описание 1", new ArrayList<>(), updateGenres, new ArrayList<>());
        expectedBook = bookRepository.save(expectedBook);

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("изменять книгу в БД с авторами и жанрами")
    @Test
    void shouldUpdateBookWithAuthorsAndGenres() {

        Author author_1 = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        author_1 = em.persist(author_1);

        Author author_2 = new Author("LastName 2", "FirstName 2", "MiddleName 2");
        author_2 = em.persist(author_2);

        Author author_3 = new Author("LastName 3", "FirstName 3", "MiddleName 3");
        author_3 = em.persist(author_3);

        Genre genre_1 = new Genre("Жанр 1", "Описание 1");
        genre_1 = em.persist(genre_1);

        Genre genre_2 = new Genre("Жанр 2", "Описание 2");
        genre_2 = em.persist(genre_2);

        Genre genre_3 = new Genre("Жанр 3", "Описание 3");
        genre_3 = em.persist(genre_3);

        List<Author> createdAuthors = new ArrayList<>();
        createdAuthors.add(author_1);
        createdAuthors.add(author_2);

        List<Genre> createdGenres = new ArrayList<>();
        createdGenres.add(genre_1);
        createdGenres.add(genre_2);

        Book createdBook = new Book("Название книги", "123", "Описание", createdAuthors, createdGenres, new ArrayList<>());
        long bookId = em.persistAndGetId(createdBook, Long.class);

        List<Author> updatedAuthors = new ArrayList<>();
        updatedAuthors.add(author_3);

        List<Genre> updateGenres = new ArrayList<>();
        updateGenres.add(genre_3);

        Book expectedBook = new Book(bookId, "Название книги 1", "1234", "Описание 1", updatedAuthors, updateGenres, new ArrayList<>());
        expectedBook = bookRepository.save(expectedBook);
        em.flush();

        Book actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

    }

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {

        Author author = new Author(EXISTING_AUTHOR_LAST_NAME, EXISTING_AUTHOR_FIRST_NAME, EXISTING_AUTHOR_MIDDLE_NAME);
        author.setId(EXISTING_AUTHOR_ID);

        Genre genre = new Genre(EXISTING_GENRE_NAME, EXISTING_GENRE_DESCRIPTION);
        genre.setId(EXISTING_GENRE_ID);

        Book expectedBook = new Book(EXISTING_BOOK_ID, EXISTING_BOOK_NAME, EXISTING_BOOK_ISBN, EXISTING_BOOK_DESCRIPTION, List.of(author), List.of(genre), new ArrayList<>());
        Optional<Book> actualBook = bookRepository.findById(expectedBook.getId());
        actualBook.get().setComments(new ArrayList<>());
        assertThat(actualBook.get()).usingRecursiveComparison().isEqualTo(expectedBook).ignoringFields();

    }

    @DisplayName("удалять заданную книгу по её id")
    @Test
    void shouldCorrectDeleteBookById() {

        Book expectedBook = new Book("Название книги", "123", "Описание", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        long bookId = em.persistAndGetId(expectedBook, Long.class);

        assertThat(bookId).isNotEqualTo(0);

        bookRepository.deleteById(bookId);
        em.flush();
        em.detach(expectedBook);

        Book book = em.find(Book.class, bookId);
        assertThat(book).isNull();
    }

    @DisplayName("вернуть все книги")
    @Test
    void shouldFindAllBooks() {

        List<Book> books = bookRepository.findAll();

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

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("ы");

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.isbn.containsIgnoreCase("353");

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.authors.any().id.eq(3L);

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.genres.any().id.eq(3L);

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("ы")
                .and(QBook.book.isbn.containsIgnoreCase("353"));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("ы")
                .and(QBook.book.isbn.containsIgnoreCase("353"))
                .and(QBook.book.authors.any().id.eq(3L));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("ы")
                .and(QBook.book.isbn.containsIgnoreCase("353"))
                .and(QBook.book.authors.any().id.eq(3L))
                .and(QBook.book.genres.any().id.eq(3L));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("Ы");

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("Ы")
                .and(QBook.book.isbn.containsIgnoreCase("353"));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("Ы")
                .and(QBook.book.isbn.containsIgnoreCase("353"))
                .and(QBook.book.authors.any().id.eq(3L));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.containsIgnoreCase("Ы")
                .and(QBook.book.isbn.containsIgnoreCase("353"))
                .and(QBook.book.authors.any().id.eq(3L))
                .and(QBook.book.genres.any().id.eq(3L));

        List<Book> books = bookRepository.findAll(predicate);

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

    @DisplayName("найти всех авторов по равенству названия")
    @Test
    void shouldFindAllBooksEqualsTitle() {

        BooleanExpression predicate = QBook.book.title.equalsIgnoreCase("Сказка о рыбаке и рыбке");

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.isbn.equalsIgnoreCase("978-5-353-08602-4");

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.authors.any().id.eq(3L);

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.genres.any().id.eq(3L);

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.equalsIgnoreCase("Сказка о рыбаке и рыбке")
                .and(QBook.book.isbn.equalsIgnoreCase("978-5-353-08602-4"));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.equalsIgnoreCase("Сказка о рыбаке и рыбке")
                .and(QBook.book.isbn.equalsIgnoreCase("978-5-353-08602-4"))
                .and(QBook.book.authors.any().id.eq(3L));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.equalsIgnoreCase("Сказка о рыбаке и рыбке")
                .and(QBook.book.isbn.equalsIgnoreCase("978-5-353-08602-4"))
                .and(QBook.book.authors.any().id.eq(3L))
                .and(QBook.book.genres.any().id.eq(3L));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.equalsIgnoreCase("СКАЗКА о рыбаке и рыбке");

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.equalsIgnoreCase("СКАЗКА о рыбаке и рыбке")
                .and(QBook.book.isbn.equalsIgnoreCase("978-5-353-08602-4"));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.equalsIgnoreCase("СКАЗКА о рыбаке и рыбке")
                .and(QBook.book.isbn.equalsIgnoreCase("978-5-353-08602-4"))
                .and(QBook.book.authors.any().id.eq(3L));

        List<Book> books = bookRepository.findAll(predicate);

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

        BooleanExpression predicate = QBook.book.title.equalsIgnoreCase("СКАЗКА о рыбаке и рыбке")
                .and(QBook.book.isbn.equalsIgnoreCase("978-5-353-08602-4"))
                .and(QBook.book.authors.any().id.eq(3L))
                .and(QBook.book.genres.any().id.eq(3L));

        List<Book> books = bookRepository.findAll(predicate);

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

}
