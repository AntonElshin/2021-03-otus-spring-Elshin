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
import ru.otus.homework.dto.GenreReqDTO;
import ru.otus.homework.dto.GenreResDTO;
import ru.otus.homework.dto.GenreResListDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.mapper.GenreMapperImpl;
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
    private BookRepository bookRepository;

    @Mock
    private GenreMapperImpl genreMapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    @DisplayName("добавить жанр")
    @Test
    void addGenre() {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр", "Описание");
        GenreResDTO genreResDTO = new GenreResDTO(1L, "Жанр", "Описание");

        Genre genre = new Genre("Жанр", "Описание");
        Genre createdGenre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findByNameEqualsIgnoreCase("Жанр")).willReturn(new ArrayList<>());
        given(genreMapper.fromDto(genreReqDTO)).willReturn(genre);
        given(genreRepository.save(genre)).willReturn(createdGenre);
        given(genreMapper.toDto(createdGenre)).willReturn(genreResDTO);

        genreService.add(genreReqDTO);

        Mockito.verify(genreRepository, Mockito.times(1)).findByNameEqualsIgnoreCase("Жанр");
        Mockito.verify(genreMapper, Mockito.times(1)).fromDto(genreReqDTO);
        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);
        Mockito.verify(genreMapper, Mockito.times(1)).toDto(createdGenre);

    }

    @DisplayName("не создать жанр из-за неуникального названия жанра")
    @Test
    void addGenreNotUniqueNameError() {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр", "Описание");

        Genre genre = new Genre(1L, "Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(genreRepository.findByNameEqualsIgnoreCase("Жанр")).willReturn(genres);

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.add(genreReqDTO);

        });

    }

    @DisplayName("изменить жанр без проверки уникальности названия жанра")
    @Test
    void updateGenreWithoutNameCheck() {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр", "Описание");
        GenreResDTO genreResDTO = new GenreResDTO(1L, "Жанр", "Описание");

        Genre genre = new Genre(1L, "Жанр", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));
        given(genreMapper.fromDto(genreReqDTO)).willReturn(genre);
        given(genreRepository.save(genre)).willReturn(genre);
        given(genreMapper.toDto(genre)).willReturn(genreResDTO);

        genreService.update(1L, genreReqDTO);

        Mockito.verify(genreRepository, Mockito.times(0)).findByNameEqualsIgnoreCase("Жанр");
        Mockito.verify(genreMapper, Mockito.times(1)).fromDto(genreReqDTO);
        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);
        Mockito.verify(genreMapper, Mockito.times(1)).toDto(genre);

    }

    @DisplayName("изменить жанр с проверки уникальности названия жанра")
    @Test
    void updateGenreWithNameCheck() {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр 1", "Описание");
        GenreResDTO genreResDTO = new GenreResDTO(1L, "Жанр 1", "Описание");

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");
        Genre updateGenre = new Genre(1L, "Жанр 1", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.of(foundGenre));
        given(genreRepository.findByNameEqualsIgnoreCase("Жанр 1")).willReturn(new ArrayList<>());
        given(genreMapper.fromDto(genreReqDTO)).willReturn(updateGenre);
        given(genreRepository.save(updateGenre)).willReturn(updateGenre);
        given(genreMapper.toDto(updateGenre)).willReturn(genreResDTO);

        genreService.update(1L, genreReqDTO);

        Mockito.verify(genreRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(genreRepository, Mockito.times(1)).findByNameEqualsIgnoreCase("Жанр 1");
        Mockito.verify(genreMapper, Mockito.times(1)).fromDto(genreReqDTO);
        Mockito.verify(genreRepository, Mockito.times(1)).save(updateGenre);
        Mockito.verify(genreMapper, Mockito.times(1)).toDto(updateGenre);

    }

    @DisplayName("не изменить жанр из-за нулевого идентификатора")
    @Test
    void updateGenreZeroIdError() {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр", "Описание");

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(0, genreReqDTO);

        });

    }

    @DisplayName("не изменить жанр из-за несуществующего идентификатора")
    @Test
    void updateGenreInvalidIdError() {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр", "Описание");

        given(genreRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(1, genreReqDTO);

        });

    }

    @DisplayName("не изменить жанр из-за неуникального названия жанра")
    @Test
    void updateGenreNotUniqueNameError() {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр 1", "Описание");

        Genre foundGenre = new Genre(1L, "Жанр", "Описание");
        Genre genre = new Genre(2L, "Жанр 1", "Описание 1");

        given(genreRepository.findById(1L)).willReturn(Optional.of(foundGenre));
        given(genreRepository.findByNameEqualsIgnoreCase("Жанр 1")).willReturn(List.of(genre));

        Assertions.assertThrows(BusinessException.class, () -> {

            genreService.update(1, genreReqDTO);

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

        GenreResListDTO genreResListDTO = new GenreResListDTO(1L, "Жанр");
        List<GenreResListDTO> genresResListDTO = new ArrayList<>();
        genresResListDTO.add(genreResListDTO);

        Genre genre = new Genre(1L, "Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(genreRepository.findByNameContainingIgnoreCase(("Жанр"))).willReturn(genres);
        given(genreMapper.toListDto(genres)).willReturn(genresResListDTO);

        genreService.findByParams("Жанр");

        Mockito.verify(genreRepository, Mockito.times(1)).findByNameContainingIgnoreCase("Жанр");
        Mockito.verify(genreMapper, Mockito.times(1)).toListDto(genres);

    }

    @DisplayName("вернёт все жанры")
    @Test
    void findAll() {

        GenreResListDTO genreResListDTO = new GenreResListDTO(1L, "Жанр");
        List<GenreResListDTO> genresResListDTO = new ArrayList<>();
        genresResListDTO.add(genreResListDTO);

        Genre genre = new Genre(1L, "Жанр", "Описание");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        given(genreRepository.findAll()).willReturn(genres);
        given(genreMapper.toListDto(genres)).willReturn(genresResListDTO);

        genreService.findAll();

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();
        Mockito.verify(genreMapper, Mockito.times(1)).toListDto(genres);

    }

}
