package ru.otus.homework.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с жанрами должен")
@ExtendWith(MockitoExtension.class)
public class GenreServiceImplTest {

    @Mock
    private GenreDao genreDao;

    @Mock
    private PrintService printService;

    @InjectMocks
    private GenreServiceImpl genreService;

    @DisplayName("добавить жанр")
    @Test
    void addGenre() {

        Genre genre = new Genre("Жанр", "Описание");

        given(genreDao.getByParamsEqualsIgnoreCase("Жанр")).willReturn(new ArrayList<>());
        Mockito.doNothing().when(genreDao).insert(0, genre);

        genreService.add("Жанр", "Описание");

        Mockito.verify(genreDao, Mockito.times(1)).getByParamsEqualsIgnoreCase("Жанр");
        Mockito.verify(genreDao, Mockito.times(1)).insert(0, genre);

    }

    @DisplayName("не создать жанр из-за null в названия жанра")
    @Test
    void addGenreNullNameError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.add(null, "Описание");

        });

    }

    @DisplayName("не создать жанр из-за пустого названия жанра")
    @Test
    void addGenreEmptyNameError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.add("", "Описание");

        });

    }

    @DisplayName("не создать жанр из-за пустого названия жанра без учёта пробелов")
    @Test
    void addGenreEmptyNameIgnoreSpacesError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.add("  ", "Описание");

        });

    }

    @DisplayName("не создать жанр из-за неуникального названия жанра")
    @Test
    void addGenreNotUniqueNameError() {

        Genre genre = new Genre("Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(genreDao.getByParamsEqualsIgnoreCase("Жанр")).willReturn(genres);

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.add("Жанр", "Описание");

        });

    }

    @DisplayName("изменить жанр без проверки уникальности названия жанра")
    @Test
    void updateGenreWithoutNameCheck() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreDao.getById(1L)).willReturn(genre);
        Mockito.doNothing().when(genreDao).update(1L, genre);

        genreService.update(1L, "Жанр", "Описание 1");

        Mockito.verify(genreDao, Mockito.times(0)).getByParamsEqualsIgnoreCase("Жанр");
        Mockito.verify(genreDao, Mockito.times(1)).update(1L, genre);

    }

    @DisplayName("изменить жанр с проверки уникальности названия жанра")
    @Test
    void updateGenreWithNameCheck() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");
        Genre updateGenre = new Genre(1L, "Жанр 1", "Описание");

        given(genreDao.getById(1L)).willReturn(foundGenre);
        given(genreDao.getByParamsEqualsIgnoreCase("Жанр 1")).willReturn(new ArrayList<>());
        Mockito.doNothing().when(genreDao).update(1L, updateGenre);

        genreService.update(1L, "Жанр 1", "Описание");

        Mockito.verify(genreDao, Mockito.times(1)).getByParamsEqualsIgnoreCase("Жанр 1");
        Mockito.verify(genreDao, Mockito.times(1)).update(1L, updateGenre);

    }

    @DisplayName("не изменить жанр из-за нулевого идентификатора")
    @Test
    void updateGenreZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(0, "Жанр", "Описание");

        });

    }

    @DisplayName("не изменить жанр из-за несуществующего идентификатора")
    @Test
    void updateGenreInvalidIdError() {

        given(genreDao.getById(1L)).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(1, "Жанр", "Описание");

        });

    }

    @DisplayName("не изменить жанр из-за неуникального названия жанра")
    @Test
    void updateGenreNotUniqueNameError() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");
        Genre genre = new Genre(2L, "Жанр 1", "Описание 1");

        given(genreDao.getById(1L)).willReturn(foundGenre);
        given(genreDao.getByParamsEqualsIgnoreCase("Жанр 1")).willReturn(List.of(genre));

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(1, "Жанр 1", "Описание");

        });

    }

    @DisplayName("не изменить жанр при null во всех параметрах")
    @Test
    void updateGenreWithNullData() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreDao.getById(1L)).willReturn(genre);

        genreService.update(1L, null, null);

        Mockito.verify(genreDao, Mockito.times(0)).update(1L, genre);

    }

    @DisplayName("не изменить жанр при пустых строках во всех параметрах")
    @Test
    void updateGenreWithEmptyData() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreDao.getById(1L)).willReturn(genre);

        genreService.update(1L, "", "");

        Mockito.verify(genreDao, Mockito.times(0)).update(1L, genre);

    }

    @DisplayName("не изменить жанр при пустых строках во всех параметрах без учёта пробелов")
    @Test
    void updateGenreWithEmptyDataIgnoreSpaces() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreDao.getById(1L)).willReturn(genre);

        genreService.update(1L, "  ", "  ");

        Mockito.verify(genreDao, Mockito.times(0)).update(1L, genre);

    }

    @DisplayName("вернуть существующий жанр по переданному идентификатору")
    @Test
    void getById() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");

        given(genreDao.getById(1L)).willReturn(foundGenre);
        given(printService.printGenre(foundGenre)).willReturn("");

        genreService.getById(1L);

        Mockito.verify(genreDao, Mockito.times(1)).getById(1L);
        Mockito.verify(printService, Mockito.times(1)).printGenre(foundGenre);

    }

    @DisplayName("не вернуть жанр из-за нулевого идентификатора")
    @Test
    void getByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.getById(0);

        });

    }

    @DisplayName("не вернуть жанр из-за несуществующего идентификатора")
    @Test
    void getByIdInvalidIdError() {

        given(genreDao.getById(1L)).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.getById(1L);

        });

    }

    @DisplayName("удалить жанр по переданному идентификатору")
    @Test
    void deleteById() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");

        given(genreDao.getById(1L)).willReturn(foundGenre);
        given(genreDao.getLinkedBookCount(1L)).willReturn(0);
        Mockito.doNothing().when(genreDao).deleteById(1L);

        genreService.deleteById(1L);

        Mockito.verify(genreDao, Mockito.times(1)).getLinkedBookCount(1L);
        Mockito.verify(genreDao, Mockito.times(1)).getById(1L);
        Mockito.verify(genreDao, Mockito.times(1)).deleteById(1L);

    }

    @DisplayName("не удалить жанр из-за нулевого идентификатора")
    @Test
    void deleteByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.deleteById(0);

        });

    }

    @DisplayName("не удалить жанр из-за несуществующего идентификатора")
    @Test
    void deleteByIdInvalidIdError() {

        given(genreDao.getById(1L)).willReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.deleteById(1L);

        });

    }

    @DisplayName("не удалить жанр из-за наличия связи с книгами")
    @Test
    void deleteByIdLinkedBookCountError() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");

        given(genreDao.getById(1L)).willReturn(foundGenre);
        given(genreDao.getLinkedBookCount(1L)).willReturn(1);


        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.deleteById(1L);

        });

    }

    @DisplayName("найдёт жанры по параметрам")
    @Test
    void findByParams() {

        Genre genre = new Genre(1L, "Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(genreDao.getByParamsLikeIgnoreCase("Жанр")).willReturn(genres);
        given(printService.printGenres(genres)).willReturn("");

        genreService.findByParams("Жанр");

        Mockito.verify(genreDao, Mockito.times(1)).getByParamsLikeIgnoreCase("Жанр");
        Mockito.verify(printService, Mockito.times(1)).printGenres(genres);

    }

    @DisplayName("вернёт все жанры")
    @Test
    void findAll() {

        Genre genre = new Genre(1L, "Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(genreDao.getAll()).willReturn(genres);
        given(printService.printGenres(genres)).willReturn("");

        genreService.findAll();

        Mockito.verify(genreDao, Mockito.times(1)).getAll();
        Mockito.verify(printService, Mockito.times(1)).printGenres(genres);

    }

    @DisplayName("вернёт количество жанров")
    @Test
    void count() {

        given(genreDao.count()).willReturn(0);
        given(printService.printGenresCount(0)).willReturn("");

        genreService.count();

        Mockito.verify(genreDao, Mockito.times(1)).count();
        Mockito.verify(printService, Mockito.times(1)).printGenresCount(0);

    }

}
