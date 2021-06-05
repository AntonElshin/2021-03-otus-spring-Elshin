package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с авторами должно")
@DataJpaTest
@Import(AuthorRepositoryJpaImpl.class)
public class AuthorRepositoryJpaTest {

    private static final int EXPECTED_AUTHORS_COUNT = 5;

    private static final int EXISTING_AUTHOR_ID = 3;
    private static final String EXISTING_AUTHOR_LAST_NAME = "Пушкин";
    private static final String EXISTING_AUTHOR_FIRST_NAME = "Александр";
    private static final String EXISTING_AUTHOR_MIDDLE_NAME = "Сергеевич";

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("возвращать ожидаемое количество авторов")
    @Test
    void shouldReturnExpectedAuthorCount() {
        Long actualAuthorsCount = authorRepositoryJpa.count();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        Author expectedAuthor = new Author("LastName", "FirstName", "MiddleName");
        expectedAuthor = authorRepositoryJpa.save(expectedAuthor);
        long authorId = expectedAuthor.getId();

        Author actualAuthor = em.find(Author.class, authorId);
        assertThat(actualAuthor).isNotNull();
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("изменять автора в БД")
    @Test
    void shouldUpdateAuthor() {
        Author expectedAuthor = new Author("LastName", "FirstName", "MiddleName");
        long authorId = em.persistAndGetId(expectedAuthor, Long.class);

        expectedAuthor = new Author(authorId, "LastName 1", "FirstName 1", "MiddleName 1");
        expectedAuthor = authorRepositoryJpa.save(expectedAuthor);

        Author actualAuthor = em.find(Author.class, authorId);
        assertThat(actualAuthor).isNotNull();
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_LAST_NAME, EXISTING_AUTHOR_FIRST_NAME, EXISTING_AUTHOR_MIDDLE_NAME);
        expectedAuthor.setId(EXISTING_AUTHOR_ID);
        Optional<Author> actualAuthor = authorRepositoryJpa.findById(EXISTING_AUTHOR_ID);
        assertThat(actualAuthor.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    void shouldCorrectDeleteAuthorById() {

        Author expectedAuthor = new Author("LastName", "FirstName", "MiddleName");
        long authorId = em.persistAndGetId(expectedAuthor, Long.class);

        assertThat(authorId).isNotEqualTo(0);

        authorRepositoryJpa.deleteById(authorId);
        em.flush();
        em.detach(expectedAuthor);

        Author author = em.find(Author.class, authorId);
        assertThat(author).isNull();
    }

    @DisplayName("вернуть всех авторов")
    @Test
    void shouldFindAllAuthors() {

        List<Author> authors = authorRepositoryJpa.findAll();

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase(null, null, null);

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase("в", null, null);

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase(null, "а", null);

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase(null, null, "о");

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase("в", "и", null);

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase("в", "и", "д");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(4L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов по части фамилии  без учёта регистра")
    @Test
    void shouldFindAllAuthorsLikePartLastNameIgnoreCase() {

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase("В", null, null);

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase(null, "А", null);

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase(null, null, "О");

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase("В", "И", null);

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

        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase("В", "И", "Д");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(4L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов при поиске без параметров (равенство)")
    @Test
    void shouldFindAllAuthorsEquals() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase(null, null, null);

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

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase("Полевой", null, null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству имени")
    @Test
    void shouldFindAllAuthorsEqualsFirstName() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase(null, "Борис", null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству отчества")
    @Test
    void shouldFindAllAuthorsEqualsMiddleName() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase(null, null, "Николаевич");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии и имени")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstName() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase("Полевой", "Борис", null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии, имени, отчёству")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameAndMiddleName() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase("Полевой", "Борис", "Николаевич");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии  без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameIgnoreCase() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase("Полевой", null, null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству имени  без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsFirstNameIgnoreCase() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase(null, "Борис", null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству отчества без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsMiddleNameIgnoreCase() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase(null, null, "николаевич");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии и имени без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameIgnoreCase() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase("полевой", "борис", null);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии, имени, отчёству без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameAndMiddleNameIgnoreCase() {

        List<Author> authors = authorRepositoryJpa.findByParamsEqualsIgnoreCase("полевой", "борис", "николаевич");

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по идентификаторам")
    @Test
    void shouldFindAllAuthorsByIds() {

        List<Long> authorStrList = new ArrayList<>();
        authorStrList.add(1L);
        authorStrList.add(2L);
        authorStrList.add(3L);

        List<Author> authors = authorRepositoryJpa.findByIds(authorStrList);

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

}
