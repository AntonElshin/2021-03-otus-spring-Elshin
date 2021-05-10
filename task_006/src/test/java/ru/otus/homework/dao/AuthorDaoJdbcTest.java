package ru.otus.homework.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.otus.homework.domain.Author;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с авторами должно")
@JdbcTest
@Import(AuthorDaoJdbc.class)
public class AuthorDaoJdbcTest {

    private static final int EXPECTED_AUTHORS_COUNT = 5;

    private static final int EXISTING_AUTHOR_ID = 3;
    private static final String EXISTING_AUTHOR_LAST_NAME = "Пушкин";
    private static final String EXISTING_AUTHOR_FIRST_NAME = "Александр";
    private static final String EXISTING_AUTHOR_MIDDLE_NAME = "Сергеевич";

    @Autowired
    private AuthorDaoJdbc authorDao;

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @DisplayName("возвращать ожидаемое количество авторов")
    @Test
    void shouldReturnExpectedAuthorCount() {
        int actualAuthorsCount = authorDao.count();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        Author expectedAuthor = new Author("LastName", "FirstName", "MiddleName");
        long authorId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId, expectedAuthor);
        expectedAuthor.setId(authorId);

        Author actualAuthor = authorDao.getById(authorId);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("изменять автора в БД")
    @Test
    void shouldUpdateAuthor() {
        Author expectedAuthor = new Author("LastName", "FirstName", "MiddleName");
        long authorId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId, expectedAuthor);
        expectedAuthor.setId(authorId);

        expectedAuthor = new Author("LastName 1", "FirstName 1", "MiddleName 1");
        authorDao.update(authorId, expectedAuthor);
        expectedAuthor.setId(authorId);

        Author actualAuthor = authorDao.getById(authorId);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_LAST_NAME, EXISTING_AUTHOR_FIRST_NAME, EXISTING_AUTHOR_MIDDLE_NAME);
        expectedAuthor.setId(EXISTING_AUTHOR_ID);
        Author actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    void shouldCorrectDeleteAuthorById() {

        Author expectedAuthor = new Author("LastName", "FirstName", "MiddleName");
        long authorId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_authors_seq')", Long.class);
        authorDao.insert(authorId, expectedAuthor);
        expectedAuthor.setId(authorId);

        assertThatCode(() -> authorDao.getById(authorId))
                .doesNotThrowAnyException();

        authorDao.deleteById(authorId);

        assertThatThrownBy(() -> authorDao.getById(authorId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("вернуть всех авторов")
    @Test
    void shouldFindAllAuthors() {

        List<Author> authors = authorDao.getAll();

        assertThat(authors).hasSize(5);

        assertThat(authors.get(0).getId()).isEqualTo(1L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Конан");

        assertThat(authors.get(1).getId()).isEqualTo(2L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(2).getId()).isEqualTo(3L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Александр");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Сергеевич");

        assertThat(authors.get(3).getId()).isEqualTo(4L);
        assertThat(authors.get(3).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(3).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(3).getMiddleName()).containsIgnoringCase("Андреевич");

        assertThat(authors.get(4).getId()).isEqualTo(5L);
        assertThat(authors.get(4).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(authors.get(4).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(authors.get(4).getMiddleName()).containsIgnoringCase("Тимофеевич");

    }

    @DisplayName("найти всех авторов при поиске без параметров (like)")
    @Test
    void shouldFindAllAuthorsLike() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase(null, null, null);

        assertThat(authors).hasSize(5);

        assertThat(authors.get(0).getId()).isEqualTo(1L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Конан");

        assertThat(authors.get(1).getId()).isEqualTo(2L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(2).getId()).isEqualTo(3L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Александр");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Сергеевич");

        assertThat(authors.get(3).getId()).isEqualTo(4L);
        assertThat(authors.get(3).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(3).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(3).getMiddleName()).containsIgnoringCase("Андреевич");

        assertThat(authors.get(4).getId()).isEqualTo(5L);
        assertThat(authors.get(4).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(authors.get(4).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(authors.get(4).getMiddleName()).containsIgnoringCase("Тимофеевич");

    }

    @DisplayName("найти всех авторов по части фамилии")
    @Test
    void shouldFindAllAuthorsLikePartLastName() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase("в", null, null);

        assertThat(authors).hasSize(3);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(1).getId()).isEqualTo(4L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Андреевич");

        assertThat(authors.get(2).getId()).isEqualTo(5L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");

    }

    @DisplayName("найти всех авторов по части имени")
    @Test
    void shouldFindAllAuthorsLikePartFirstName() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase(null, "а", null);

        assertThat(authors).hasSize(3);

        assertThat(authors.get(0).getId()).isEqualTo(1L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Конан");

        assertThat(authors.get(1).getId()).isEqualTo(3L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Александр");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Сергеевич");

        assertThat(authors.get(2).getId()).isEqualTo(4L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов по части отчества")
    @Test
    void shouldFindAllAuthorsLikePartMiddleName() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase(null, null, "о");

        assertThat(authors).hasSize(3);

        assertThat(authors.get(0).getId()).isEqualTo(1L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Конан");

        assertThat(authors.get(1).getId()).isEqualTo(2L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(2).getId()).isEqualTo(5L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");

    }

    @DisplayName("найти всех авторов по части фамилии и имени")
    @Test
    void shouldFindAllAuthorsLikePartLastNameAndFirstName() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase("в", "и", null);

        assertThat(authors).hasSize(2);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(1).getId()).isEqualTo(4L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов по части фамилии, имени, отчества")
    @Test
    void shouldFindAllAuthorsLikePartLastNameAndFirstNameAndMiddleName() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase("в", "и", "д");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(4L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов по части фамилии  без учёта регистра")
    @Test
    void shouldFindAllAuthorsLikePartLastNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase("В", null, null);

        assertThat(authors).hasSize(3);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(1).getId()).isEqualTo(4L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Андреевич");

        assertThat(authors.get(2).getId()).isEqualTo(5L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");

    }

    @DisplayName("найти всех авторов по части имени  без учёта регистра")
    @Test
    void shouldFindAllAuthorsLikePartFirstNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase(null, "А", null);

        assertThat(authors).hasSize(3);

        assertThat(authors.get(0).getId()).isEqualTo(1L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Конан");

        assertThat(authors.get(1).getId()).isEqualTo(3L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Александр");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Сергеевич");

        assertThat(authors.get(2).getId()).isEqualTo(4L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов по части отчества  без учёта регистра")
    @Test
    void shouldFindAllAuthorsLikePartMiddleNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase(null, null, "О");

        assertThat(authors).hasSize(3);

        assertThat(authors.get(0).getId()).isEqualTo(1L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Конан");

        assertThat(authors.get(1).getId()).isEqualTo(2L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(2).getId()).isEqualTo(5L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Тимофеевич");

    }

    @DisplayName("найти всех авторов по части фамилии и имени  без учёта регистра")
    @Test
    void shouldFindAllAuthorsLikePartLastNameAndFirstNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase("В", "И", null);

        assertThat(authors).hasSize(2);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(1).getId()).isEqualTo(4L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов по части фамилии, имени, отчества без учёта регистра")
    @Test
    void shouldFindAllAuthorsLikePartLastNameAndFirstNameAndMiddleNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsLikeIgnoreCase("В", "И", "Д");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(4L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов при поиске без параметров (равенство)")
    @Test
    void shouldFindAllAuthorsEquals() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase(null, null, null);

        assertThat(authors).hasSize(5);

        assertThat(authors.get(0).getId()).isEqualTo(1L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Конан");

        assertThat(authors.get(1).getId()).isEqualTo(2L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(2).getId()).isEqualTo(3L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Александр");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Сергеевич");

        assertThat(authors.get(3).getId()).isEqualTo(4L);
        assertThat(authors.get(3).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(3).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(3).getMiddleName()).containsIgnoringCase("Андреевич");

        assertThat(authors.get(4).getId()).isEqualTo(5L);
        assertThat(authors.get(4).getLastName()).containsIgnoringCase("Аксаков");
        assertThat(authors.get(4).getFirstName()).containsIgnoringCase("Сергей");
        assertThat(authors.get(4).getMiddleName()).containsIgnoringCase("Тимофеевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии")
    @Test
    void shouldFindAllAuthorsEqualsLastName() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase("Полевой", null, null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству имени")
    @Test
    void shouldFindAllAuthorsEqualsFirstName() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase(null, "Борис", null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству отчества")
    @Test
    void shouldFindAllAuthorsEqualsMiddleName() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase(null, null, "Николаевич");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии и имени")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstName() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase("Полевой", "Борис", null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии, имени, отчёству")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameAndMiddleName() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase("Полевой", "Борис", "Николаевич");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии  без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase("Полевой", null, null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству имени  без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsFirstNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase(null, "Борис", null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству отчества без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsMiddleNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase(null, null, "николаевич");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии и имени без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase("полевой", "борис", null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии, имени, отчёству без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameAndMiddleNameIgnoreCase() {

        List<Author> authors = authorDao.getByParamsEqualsIgnoreCase("полевой", "борис", "николаевич");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по идентификаторам")
    @Test
    void shouldFindAllAuthorsByIds() {

        List<String> authorStrList = new ArrayList<>();
        authorStrList.add("1");
        authorStrList.add("2");
        authorStrList.add("3");

        List<Author> authors = authorDao.getByIds(authorStrList);

        assertThat(authors).hasSize(3);

        assertThat(authors.get(0).getId()).isEqualTo(1L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Дойл");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Артур");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Конан");

        assertThat(authors.get(1).getId()).isEqualTo(2L);
        assertThat(authors.get(1).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(1).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(1).getMiddleName()).containsIgnoringCase("Николаевич");

        assertThat(authors.get(2).getId()).isEqualTo(3L);
        assertThat(authors.get(2).getLastName()).containsIgnoringCase("Пушкин");
        assertThat(authors.get(2).getFirstName()).containsIgnoringCase("Александр");
        assertThat(authors.get(2).getMiddleName()).containsIgnoringCase("Сергеевич");

    }

    @DisplayName("найти количество книг по идентификатору автора")
    @Test
    void shouldFindBookCountByAuthorId() {

        assertThat(authorDao.getLinkedBookCount(1L)).isEqualTo(1);
        assertThat(authorDao.getLinkedBookCount(2L)).isEqualTo(1);
        assertThat(authorDao.getLinkedBookCount(3L)).isEqualTo(2);
        assertThat(authorDao.getLinkedBookCount(4L)).isEqualTo(1);
        assertThat(authorDao.getLinkedBookCount(5L)).isEqualTo(1);

    }

}
