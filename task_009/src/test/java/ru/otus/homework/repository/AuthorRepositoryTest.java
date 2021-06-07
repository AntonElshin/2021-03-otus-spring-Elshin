package ru.otus.homework.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.domain.QAuthor;
import ru.otus.homework.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Dao для работы с авторами должно")
@DataJpaTest
public class AuthorRepositoryTest {

    private static final int EXPECTED_AUTHORS_COUNT = 5;

    private static final Long EXISTING_AUTHOR_ID = 3L;
    private static final String EXISTING_AUTHOR_LAST_NAME = "Пушкин";
    private static final String EXISTING_AUTHOR_FIRST_NAME = "Александр";
    private static final String EXISTING_AUTHOR_MIDDLE_NAME = "Сергеевич";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("возвращать ожидаемое количество авторов")
    @Test
    void shouldReturnExpectedAuthorCount() {
        Long actualAuthorsCount = authorRepository.count();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        Author expectedAuthor = new Author("LastName", "FirstName", "MiddleName");
        expectedAuthor = authorRepository.save(expectedAuthor);
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
        expectedAuthor = authorRepository.save(expectedAuthor);

        Author actualAuthor = em.find(Author.class, authorId);
        assertThat(actualAuthor).isNotNull();
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_LAST_NAME, EXISTING_AUTHOR_FIRST_NAME, EXISTING_AUTHOR_MIDDLE_NAME);
        expectedAuthor.setId(EXISTING_AUTHOR_ID);
        Optional<Author> actualAuthor = authorRepository.findById(EXISTING_AUTHOR_ID);
        assertThat(actualAuthor.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    void shouldCorrectDeleteAuthorById() {

        Author expectedAuthor = new Author("LastName", "FirstName", "MiddleName");
        long authorId = em.persistAndGetId(expectedAuthor, Long.class);

        assertThat(authorId).isNotEqualTo(0);

        authorRepository.deleteById(authorId);
        em.flush();
        em.detach(expectedAuthor);

        Author author = em.find(Author.class, authorId);
        assertThat(author).isNull();
    }

    @DisplayName("вернуть всех авторов")
    @Test
    void shouldFindAllAuthors() {

        List<Author> authors = authorRepository.findAll();

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

        List<Author> authors = authorRepository.findAll();

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

        BooleanExpression predicate = QAuthor.author.lastName.containsIgnoreCase("в");

        List<Author> authors = authorRepository.findAll(predicate);

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

        BooleanExpression predicate = QAuthor.author.firstName.containsIgnoreCase("а");

        List<Author> authors = authorRepository.findAll(predicate);

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

        BooleanExpression predicate = QAuthor.author.middleName.containsIgnoreCase("о");

        List<Author> authors = authorRepository.findAll(predicate);

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

        BooleanExpression predicate = QAuthor.author.lastName.containsIgnoreCase("в")
                .and(QAuthor.author.firstName.containsIgnoreCase("и"));

        List<Author> authors = authorRepository.findAll(predicate);

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

        BooleanExpression predicate = QAuthor.author.lastName.containsIgnoreCase("в")
                .and(QAuthor.author.firstName.containsIgnoreCase("и"))
                .and(QAuthor.author.middleName.containsIgnoreCase("д")
        );

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(4L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов по части фамилии  без учёта регистра")
    @Test
    void shouldFindAllAuthorsLikePartLastNameIgnoreCase() {

        BooleanExpression predicate = QAuthor.author.lastName.containsIgnoreCase("В");

        List<Author> authors = authorRepository.findAll(predicate);

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

        BooleanExpression predicate = QAuthor.author.firstName.containsIgnoreCase("А");

        List<Author> authors = authorRepository.findAll(predicate);

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

        BooleanExpression predicate = QAuthor.author.middleName.containsIgnoreCase("О");

        List<Author> authors = authorRepository.findAll(predicate);

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

        BooleanExpression predicate = QAuthor.author.lastName.containsIgnoreCase("В")
                .and(QAuthor.author.firstName.containsIgnoreCase("И"));

        List<Author> authors = authorRepository.findAll(predicate);

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

        BooleanExpression predicate = QAuthor.author.lastName.containsIgnoreCase("В")
                .and(QAuthor.author.firstName.containsIgnoreCase("И"))
                .and(QAuthor.author.middleName.containsIgnoreCase("Д")
        );

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(4L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Жуковский");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Василий");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Андреевич");

    }

    @DisplayName("найти всех авторов при поиске без параметров (равенство)")
    @Test
    void shouldFindAllAuthorsEquals() {

        List<Author> authors = authorRepository.findAll();

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

        BooleanExpression predicate = QAuthor.author.lastName.equalsIgnoreCase("Полевой");

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству имени")
    @Test
    void shouldFindAllAuthorsEqualsFirstName() {

        BooleanExpression predicate = QAuthor.author.firstName.equalsIgnoreCase("Борис");

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству отчества")
    @Test
    void shouldFindAllAuthorsEqualsMiddleName() {

        BooleanExpression predicate = QAuthor.author.middleName.equalsIgnoreCase("Николаевич");

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии и имени")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstName() {

        BooleanExpression predicate = QAuthor.author.lastName.equalsIgnoreCase("Полевой")
                .and(QAuthor.author.firstName.equalsIgnoreCase("Борис"));

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии, имени, отчёству")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameAndMiddleName() {

        BooleanExpression predicate = QAuthor.author.lastName.equalsIgnoreCase("Полевой")
                .and(QAuthor.author.firstName.equalsIgnoreCase("Борис"))
                .and(QAuthor.author.middleName.equalsIgnoreCase("Николаевич"));

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии  без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameIgnoreCase() {

        BooleanExpression predicate = QAuthor.author.lastName.equalsIgnoreCase("полевой");

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству имени  без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsFirstNameIgnoreCase() {

        BooleanExpression predicate = QAuthor.author.firstName.equalsIgnoreCase("борис");

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству отчества без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsMiddleNameIgnoreCase() {

        BooleanExpression predicate = QAuthor.author.middleName.equalsIgnoreCase("николаевич");

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии и имени без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameIgnoreCase() {

        BooleanExpression predicate = QAuthor.author.lastName.equalsIgnoreCase("полевой")
                .and(QAuthor.author.firstName.equalsIgnoreCase("борис"));

        List<Author> authors = authorRepository.findAll(predicate);

        assertThat(authors).hasSize(1);

        assertThat(authors.get(0).getId()).isEqualTo(2L);
        assertThat(authors.get(0).getLastName()).containsIgnoringCase("Полевой");
        assertThat(authors.get(0).getFirstName()).containsIgnoringCase("Борис");
        assertThat(authors.get(0).getMiddleName()).containsIgnoringCase("Николаевич");

    }

    @DisplayName("найти всех авторов по равенству фамилии, имени, отчёству без учёта регистра")
    @Test
    void shouldFindAllAuthorsEqualsLastNameAndFirstNameAndMiddleNameIgnoreCase() {

        BooleanExpression predicate = QAuthor.author.lastName.equalsIgnoreCase("полевой")
                .and(QAuthor.author.firstName.equalsIgnoreCase("борис"))
                .and(QAuthor.author.middleName.equalsIgnoreCase("николаевич"));

        List<Author> authors = authorRepository.findAll(predicate);

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

        List<Author> authors = authorRepository.findByIds(authorStrList);

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
