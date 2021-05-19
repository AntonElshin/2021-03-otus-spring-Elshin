package ru.otus.homework.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.otus.homework.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с жанрами должно")
@JdbcTest
@Import(GenreDaoJdbc.class)
public class GenreDaoJdbcTest {

    private static final int EXPECTED_GENRES_COUNT = 3;

    private static final int EXISTING_GENRE_ID = 2;
    private static final String EXISTING_GENRE_NAME = "Повесть";
    private static final String EXISTING_GENRE_DESCRIPTION = "Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя";

    @Autowired
    private GenreDaoJdbc genreDao;

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @DisplayName("возвращать ожидаемое количество жанров")
    @Test
    void shouldReturnExpectedGenreCount() {
        int actualGenresCount = genreDao.count();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        Genre expectedGenre = new Genre("Жанр", "Описание");
        long genreId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId, expectedGenre);
        expectedGenre.setId(genreId);

        Genre actualGenre = genreDao.getById(genreId);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("изменять жанр в БД")
    @Test
    void shouldUpdateGenre() {
        Genre expectedGenre = new Genre("Жанр", "Описание");
        long genreId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId, expectedGenre);
        expectedGenre.setId(genreId);

        expectedGenre = new Genre("Жанр 1", "Описание 1");
        genreDao.update(genreId, expectedGenre);
        expectedGenre.setId(genreId);

        Genre actualGenre = genreDao.getById(genreId);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRE_NAME, EXISTING_GENRE_DESCRIPTION);
        expectedGenre.setId(EXISTING_GENRE_ID);
        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("удалять заданный жанр по его id")
    @Test
    void shouldCorrectDeleteGenreById() {

        Genre expectedGenre = new Genre("Жанр", "Описание");
        long genreId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_genres_seq')", Long.class);
        genreDao.insert(genreId, expectedGenre);
        expectedGenre.setId(genreId);

        assertThatCode(() -> genreDao.getById(genreId))
                .doesNotThrowAnyException();

        genreDao.deleteById(genreId);

        assertThatThrownBy(() -> genreDao.getById(genreId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("вернуть все жанры")
    @Test
    void shouldFindAllGenres() {

        List<Genre> genres = genreDao.getAll();

        assertThat(genres).hasSize(3);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(genres.get(1).getId()).isEqualTo(2L);
        assertThat(genres.get(1).getName()).containsIgnoringCase("Повесть");
        assertThat(genres.get(1).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

        assertThat(genres.get(2).getId()).isEqualTo(3L);
        assertThat(genres.get(2).getName()).containsIgnoringCase("Сказка");
        assertThat(genres.get(2).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");
    }

    @DisplayName("найти все жанры при поиске без параметров (like)")
    @Test
    void shouldFindAllGenresLike() {

        List<Genre> genres = genreDao.getByParamsLikeIgnoreCase(null);

        assertThat(genres).hasSize(3);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(genres.get(1).getId()).isEqualTo(2L);
        assertThat(genres.get(1).getName()).containsIgnoringCase("Повесть");
        assertThat(genres.get(1).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

        assertThat(genres.get(2).getId()).isEqualTo(3L);
        assertThat(genres.get(2).getName()).containsIgnoringCase("Сказка");
        assertThat(genres.get(2).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");
    }

    @DisplayName("найти всех жанры по части названия")
    @Test
    void shouldFindAllGenresLikePartName() {

        List<Genre> genres = genreDao.getByParamsLikeIgnoreCase("е");

        assertThat(genres).hasSize(2);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(genres.get(1).getId()).isEqualTo(2L);
        assertThat(genres.get(1).getName()).containsIgnoringCase("Повесть");
        assertThat(genres.get(1).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

    }

    @DisplayName("найти всех жанры по части названия без учёта регистра")
    @Test
    void shouldFindAllGenresLikePartNameIgnoreCase() {

        List<Genre> genres = genreDao.getByParamsLikeIgnoreCase("Е");

        assertThat(genres).hasSize(2);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(genres.get(1).getId()).isEqualTo(2L);
        assertThat(genres.get(1).getName()).containsIgnoringCase("Повесть");
        assertThat(genres.get(1).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

    }

    @DisplayName("найти жанр при поиске без параметров (равенство)")
    @Test
    void shouldFindAllGenresEquals() {

        List<Genre> genres = genreDao.getByParamsEqualsIgnoreCase(null);

        assertThat(genres).hasSize(3);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(genres.get(1).getId()).isEqualTo(2L);
        assertThat(genres.get(1).getName()).containsIgnoringCase("Повесть");
        assertThat(genres.get(1).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

        assertThat(genres.get(2).getId()).isEqualTo(3L);
        assertThat(genres.get(2).getName()).containsIgnoringCase("Сказка");
        assertThat(genres.get(2).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");


    }

    @DisplayName("найти жанр по равенству названия")
    @Test
    void shouldFindAllGenresLikeEqualsName() {

        List<Genre> genres = genreDao.getByParamsEqualsIgnoreCase("Детектив");

        assertThat(genres).hasSize(1);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

    }

    @DisplayName("найти жанр по равенству названия без учёта регистра")
    @Test
    void shouldFindAllGenresLikeEqualsNameIgnoreCase() {

        List<Genre> genres = genreDao.getByParamsEqualsIgnoreCase("детектив");

        assertThat(genres).hasSize(1);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

    }

    @DisplayName("найти все жанры по идентификаторам")
    @Test
    void shouldFindAllGenresByIds() {

        List<String> genreStrList = new ArrayList<>();
        genreStrList.add("1");
        genreStrList.add("2");
        genreStrList.add("3");

        List<Genre> genres = genreDao.getByIds(genreStrList);

        assertThat(genres).hasSize(3);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(genres.get(1).getId()).isEqualTo(2L);
        assertThat(genres.get(1).getName()).containsIgnoringCase("Повесть");
        assertThat(genres.get(1).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

        assertThat(genres.get(2).getId()).isEqualTo(3L);
        assertThat(genres.get(2).getName()).containsIgnoringCase("Сказка");
        assertThat(genres.get(2).getDescription()).containsIgnoringCase("Жанр литературного творчества, чаще всего в сказках присутствует волшебство и различные невероятные приключения");

    }

    @DisplayName("найти количество книг по идентификатору жанра")
    @Test
    void shouldFindBookCountByGenreId() {

        assertThat(genreDao.getLinkedBookCount(1L)).isEqualTo(1);
        assertThat(genreDao.getLinkedBookCount(2L)).isEqualTo(1);
        assertThat(genreDao.getLinkedBookCount(3L)).isEqualTo(2);

    }

}
