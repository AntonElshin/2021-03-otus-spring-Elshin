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
import ru.otus.homework.domain.QBook;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с жанрами должен")
@ExtendWith(MockitoExtension.class)
public class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private
    BookRepository bookRepository;

    @Mock
    private PrintGenreService printGenreService;

    @InjectMocks
    private GenreServiceImpl genreService;

    @DisplayName("добавить жанр")
    @Test
    void addGenre() {

        Genre genre = new Genre("Жанр", "Описание");
        Genre createdGenre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findByNameEqualsIgnoreCase("Жанр")).willReturn(new ArrayList<>());
        given(genreRepository.save(genre)).willReturn(createdGenre);

        genreService.add("Жанр", "Описание");

        Mockito.verify(genreRepository, Mockito.times(1)).findByNameEqualsIgnoreCase("Жанр");
        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);

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

        given(genreRepository.findByNameEqualsIgnoreCase("Жанр")).willReturn(genres);

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.add("Жанр", "Описание");

        });

    }

    @DisplayName("изменить жанр без проверки уникальности названия жанра")
    @Test
    void updateGenreWithoutNameCheck() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));
        given(genreRepository.save(genre)).willReturn(genre);

        genreService.update(1L, "Жанр", "Описание 1");

        Mockito.verify(genreRepository, Mockito.times(0)).findByNameEqualsIgnoreCase("Жанр");
        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);

    }

    @DisplayName("изменить жанр с проверки уникальности названия жанра")
    @Test
    void updateGenreWithNameCheck() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");
        Genre updateGenre = new Genre(1L, "Жанр 1", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(foundGenre));
        given(genreRepository.findByNameEqualsIgnoreCase("Жанр 1")).willReturn(new ArrayList<>());
        given(genreRepository.save(foundGenre)).willReturn(foundGenre);

        genreService.update(1L, "Жанр 1", "Описание");

        Mockito.verify(genreRepository, Mockito.times(1)).findByNameEqualsIgnoreCase("Жанр 1");
        Mockito.verify(genreRepository, Mockito.times(1)).save(updateGenre);

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

        given(genreRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(1, "Жанр", "Описание");

        });

    }

    @DisplayName("не изменить жанр из-за неуникального названия жанра")
    @Test
    void updateGenreNotUniqueNameError() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");
        Genre genre = new Genre(2L, "Жанр 1", "Описание 1");

        given(genreRepository.findById(1L)).willReturn(Optional.of(foundGenre));
        given(genreRepository.findByNameEqualsIgnoreCase("Жанр 1")).willReturn(List.of(genre));

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(1, "Жанр 1", "Описание");

        });

    }

    @DisplayName("не изменить жанр при null во всех параметрах")
    @Test
    void updateGenreWithNullData() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));

        genreService.update(1L, null, null);

        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);

    }

    @DisplayName("не изменить жанр при пустых строках во всех параметрах")
    @Test
    void updateGenreWithEmptyData() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));

        genreService.update(1L, "", "");

        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);

    }

    @DisplayName("не изменить жанр при пустых строках во всех параметрах без учёта пробелов")
    @Test
    void updateGenreWithEmptyDataIgnoreSpaces() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));

        genreService.update(1L, "  ", "  ");

        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);

    }

    @DisplayName("вернуть существующий жанр по переданному идентификатору")
    @Test
    void getById() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(foundGenre));
        given(printGenreService.printGenre(foundGenre)).willReturn("");

        genreService.getById(1L);

        Mockito.verify(genreRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(printGenreService, Mockito.times(1)).printGenre(foundGenre);

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

        given(genreRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.getById(1L);

        });

    }

    @DisplayName("удалить жанр по переданному идентификатору")
    @Test
    void deleteById() {

        Genre genre = new Genre(1L, "Жанр", "Описание");

        BooleanExpression predicate = QBook.book.genres.any().id.eq(1L);

        given(bookRepository.findAll(predicate)).willReturn(null);
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));
        Mockito.doNothing().when(genreRepository).deleteById(1L);

        genreService.deleteById(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(predicate);
        Mockito.verify(genreRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(genreRepository, Mockito.times(1)).deleteById(1L);

    }

    @DisplayName("не удалить жанр из-за нулевого идентификатора")
    @Test
    void deleteByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.deleteById(0);

        });

    }

    @DisplayName("не удалить жанр из-за наличия связи с книгами")
    @Test
    void deleteByIdLinkedBookCountError() {

        List<Book> books = new ArrayList<>();
        books.add(new Book());

        BooleanExpression predicate = QBook.book.genres.any().id.eq(1L);

        given(bookRepository.findAll(predicate)).willReturn(books);


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

        given(genreRepository.findByNameContainingIgnoreCase(("Жанр"))).willReturn(genres);
        given(printGenreService.printGenres(genres)).willReturn("");

        genreService.findByParams("Жанр");

        Mockito.verify(genreRepository, Mockito.times(1)).findByNameContainingIgnoreCase("Жанр");
        Mockito.verify(printGenreService, Mockito.times(1)).printGenres(genres);

    }

    @DisplayName("вернёт все жанры")
    @Test
    void findAll() {

        Genre genre = new Genre(1L, "Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(genreRepository.findAll()).willReturn(genres);
        given(printGenreService.printGenres(genres)).willReturn("");

        genreService.findAll();

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();
        Mockito.verify(printGenreService, Mockito.times(1)).printGenres(genres);

    }

    @DisplayName("вернёт количество жанров")
    @Test
    void count() {

        given(genreRepository.count()).willReturn(0L);
        given(printGenreService.printGenresCount(0L)).willReturn("");

        genreService.count();

        Mockito.verify(genreRepository, Mockito.times(1)).count();
        Mockito.verify(printGenreService, Mockito.times(1)).printGenresCount(0L);

    }

}
