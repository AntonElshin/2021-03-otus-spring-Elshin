package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.domain.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DisplayName("Dao для работы с жанрами должно")
@DataJpaTest
public class GenreRepositoryTest {

    private static final int EXPECTED_GENRES_COUNT = 3;

    private static final Long EXISTING_GENRE_ID = 2L;
    private static final String EXISTING_GENRE_NAME = "Повесть";
    private static final String EXISTING_GENRE_DESCRIPTION = "Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("возвращать ожидаемое количество жанров")
    @Test
    void shouldReturnExpectedGenreCount() {
        Long actualGenresCount = genreRepository.count();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        Genre expectedGenre = new Genre("Жанр", "Описание");
        expectedGenre = genreRepository.save(expectedGenre);
        long genreId = expectedGenre.getId();

        Genre actualGenre = em.find(Genre.class, genreId);
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("изменять жанр в БД")
    @Test
    void shouldUpdateGenre() {
        Genre expectedGenre = new Genre("Жанр", "Описание");
        long genreId = em.persistAndGetId(expectedGenre, Long.class);

        expectedGenre = new Genre(genreId, "Жанр 1", "Описание 1");
        expectedGenre = genreRepository.save(expectedGenre);

        Genre actualGenre = em.find(Genre.class, genreId);
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRE_NAME, EXISTING_GENRE_DESCRIPTION);
        expectedGenre.setId(EXISTING_GENRE_ID);
        Optional<Genre> actualGenre = genreRepository.findById(expectedGenre.getId());
        assertThat(actualGenre.get()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("удалять заданный жанр по его id")
    @Test
    void shouldCorrectDeleteGenreById() {

        Genre expectedGenre = new Genre("Жанр", "Описание");
        long genreId = em.persistAndGetId(expectedGenre, Long.class);

        assertThat(genreId).isNotEqualTo(0);

        genreRepository.deleteById(genreId);
        em.flush();
        em.detach(expectedGenre);

        Genre genre = em.find(Genre.class, genreId);
        assertThat(genre).isNull();
    }

    @DisplayName("вернуть все жанры")
    @Test
    void shouldFindAllGenres() {

        List<Genre> genres = genreRepository.findAll();

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

        List<Genre> genres = genreRepository.findByNameContainingIgnoreCase("е");

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

        List<Genre> genres = genreRepository.findByNameContainingIgnoreCase("Е");

        assertThat(genres).hasSize(2);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

        assertThat(genres.get(1).getId()).isEqualTo(2L);
        assertThat(genres.get(1).getName()).containsIgnoringCase("Повесть");
        assertThat(genres.get(1).getDescription()).containsIgnoringCase("Средняя форма; произведение, в котором освещается ряд событий в жизни главного героя");

    }

    @DisplayName("найти жанр по равенству названия")
    @Test
    void shouldFindAllGenresLikeEqualsName() {

        List<Genre> genres = genreRepository.findByNameEqualsIgnoreCase("Детектив");

        assertThat(genres).hasSize(1);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

    }

    @DisplayName("найти жанр по равенству названия без учёта регистра")
    @Test
    void shouldFindAllGenresLikeEqualsNameIgnoreCase() {

        List<Genre> genres = genreRepository.findByNameEqualsIgnoreCase("детектив");

        assertThat(genres).hasSize(1);

        assertThat(genres.get(0).getId()).isEqualTo(1L);
        assertThat(genres.get(0).getName()).containsIgnoringCase("Детектив");
        assertThat(genres.get(0).getDescription()).containsIgnoringCase("Преимущественно литературный жанр, произведения которого описывают процесс исследования загадочного происшествия с целью выяснения его обстоятельств и раскрытия загадки");

    }

    @DisplayName("найти все жанры по идентификаторам")
    @Test
    void shouldFindAllGenresByIds() {

        List<Long> genreStrList = new ArrayList<>();
        genreStrList.add(1L);
        genreStrList.add(2L);
        genreStrList.add(3L);

        List<Genre> genres = genreRepository.findByIds(genreStrList);

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

}
