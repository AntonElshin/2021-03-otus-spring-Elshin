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
import ru.otus.homework.dto.GenreDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.mapper.GenreMapper;
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
    private ValidationService validationService;

    @InjectMocks
    private GenreServiceImpl genreService;

    @DisplayName("добавить жанр")
    @Test
    void addGenre() {

        GenreDTO genreDTO = new GenreDTO("Жанр", "Описание");
        Genre genre = GenreMapper.INSTANCE.fromDto(genreDTO);
        Genre createdGenre = new Genre(1L, "Жанр", "Описание");

        Mockito.doNothing().when(validationService).validateDTO(genreDTO);
        given(genreRepository.findByNameEqualsIgnoreCase("Жанр")).willReturn(new ArrayList<>());
        given(genreRepository.save(genre)).willReturn(createdGenre);

        genreService.add(genreDTO);

        Mockito.verify(validationService, Mockito.times(1)).validateDTO(genreDTO);
        Mockito.verify(genreRepository, Mockito.times(1)).findByNameEqualsIgnoreCase("Жанр");
        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);

    }

    @DisplayName("не создать жанр из-за неуникального названия жанра")
    @Test
    void addGenreNotUniqueNameError() {

        GenreDTO genreDTO = new GenreDTO("Жанр", "Описание");
        Genre genre = GenreMapper.INSTANCE.fromDto(genreDTO);
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Mockito.doNothing().when(validationService).validateDTO(genreDTO);
        given(genreRepository.findByNameEqualsIgnoreCase("Жанр")).willReturn(genres);

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.add(genreDTO);

        });

    }

    @DisplayName("изменить жанр без проверки уникальности названия жанра")
    @Test
    void updateGenreWithoutNameCheck() {

        GenreDTO genreDTO = new GenreDTO(1L, "Жанр", "Описание");
        Genre genre = GenreMapper.INSTANCE.fromDto(genreDTO);

        Mockito.doNothing().when(validationService).validateDTO(genreDTO);
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));
        given(genreRepository.save(genre)).willReturn(genre);

        genreService.update(1L, genreDTO);

        Mockito.verify(validationService, Mockito.times(1)).validateDTO(genreDTO);
        Mockito.verify(genreRepository, Mockito.times(0)).findByNameEqualsIgnoreCase("Жанр");
        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);

    }

    @DisplayName("изменить жанр с проверки уникальности названия жанра")
    @Test
    void updateGenreWithNameCheck() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");

        GenreDTO genreDTO = new GenreDTO(1L, "Жанр 1", "Описание");
        Genre updateGenre = GenreMapper.INSTANCE.fromDto(genreDTO);

        Mockito.doNothing().when(validationService).validateDTO(genreDTO);
        given(genreRepository.findById(1L)).willReturn(Optional.of(foundGenre));
        given(genreRepository.findByNameEqualsIgnoreCase("Жанр 1")).willReturn(new ArrayList<>());
        given(genreRepository.save(updateGenre)).willReturn(updateGenre);

        genreService.update(1L, genreDTO);

        Mockito.verify(validationService, Mockito.times(1)).validateDTO(genreDTO);
        Mockito.verify(genreRepository, Mockito.times(1)).findByNameEqualsIgnoreCase("Жанр 1");
        Mockito.verify(genreRepository, Mockito.times(1)).save(updateGenre);

    }

    @DisplayName("не изменить жанр из-за нулевого идентификатора")
    @Test
    void updateGenreZeroIdError() {

        GenreDTO genreDTO = new GenreDTO("Жанр", "Описание");

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(0, genreDTO);

        });

    }

    @DisplayName("не изменить жанр из-за несуществующего идентификатора")
    @Test
    void updateGenreInvalidIdError() {

        GenreDTO genreDTO = new GenreDTO("Жанр", "Описание");

        Mockito.doNothing().when(validationService).validateDTO(genreDTO);
        given(genreRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(1, genreDTO);

        });

    }

    @DisplayName("не изменить жанр из-за неуникального названия жанра")
    @Test
    void updateGenreNotUniqueNameError() {

        GenreDTO genreDTO = new GenreDTO("Жанр 1", "Описание");

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");
        Genre genre = new Genre(2L, "Жанр 1", "Описание 1");

        Mockito.doNothing().when(validationService).validateDTO(genreDTO);
        given(genreRepository.findById(1L)).willReturn(Optional.of(foundGenre));
        given(genreRepository.findByNameEqualsIgnoreCase("Жанр 1")).willReturn(List.of(genre));

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(1, genreDTO);

        });

    }

    @DisplayName("вернуть существующий жанр по переданному идентификатору")
    @Test
    void getById() {

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(foundGenre));

        genreService.getById(1L);

        Mockito.verify(genreRepository, Mockito.times(1)).findById(1L);

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

        BooleanExpression predicate = QBook.book.genres.any().id.eq(1L);

        given(bookRepository.findAll(predicate)).willReturn(null);
        Mockito.doNothing().when(genreRepository).deleteById(1L);

        genreService.deleteById(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(predicate);
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

        genreService.findByParams("Жанр");

        Mockito.verify(genreRepository, Mockito.times(1)).findByNameContainingIgnoreCase("Жанр");

    }

    @DisplayName("вернёт все жанры")
    @Test
    void findAll() {

        Genre genre = new Genre(1L, "Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(genreRepository.findAll()).willReturn(genres);

        genreService.findAll();

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();

    }

}
