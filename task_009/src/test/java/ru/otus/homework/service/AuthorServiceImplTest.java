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
import ru.otus.homework.domain.QAuthor;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.QBook;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.AuthorReqDTO;
import ru.otus.homework.dto.AuthorResDTO;
import ru.otus.homework.dto.AuthorResListDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.mapper.AuthorMapperImpl;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@DisplayName("Service для работы с авторами должен")
@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapperImpl authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @DisplayName("добавить автора")
    @Test
    void addAuthor() {

        AuthorReqDTO authorReqDTO = new AuthorReqDTO("Фамилия", "Имя", "Отчество");
        AuthorResDTO authorResDTO = new AuthorResDTO(1L,"Фамилия", "Имя", "Отчество");

        Author author = new Author("Фамилия", "Имя", "Отчество");
        Author createdAuthor = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorMapper.fromDto(authorReqDTO)).willReturn(author);
        given(authorRepository.save(author)).willReturn(createdAuthor);
        given(authorMapper.toDto(createdAuthor)).willReturn(authorResDTO);

        authorService.add(authorReqDTO);

        Mockito.verify(authorMapper, Mockito.times(1)).fromDto(authorReqDTO);
        Mockito.verify(authorRepository, Mockito.times(1)).save(author);
        Mockito.verify(authorMapper, Mockito.times(1)).toDto(createdAuthor);
    }

    @DisplayName("изменить автора")
    @Test
    void updateAuthor() {

        AuthorReqDTO authorReqDTO = new AuthorReqDTO("Фамилия", "Имя", "Отчество");
        AuthorResDTO authorResDTO = new AuthorResDTO(1L,"Фамилия", "Имя", "Отчество");

        Author author = new Author(1L, "Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(authorMapper.fromDto(authorReqDTO)).willReturn(author);
        given(authorRepository.save(author)).willReturn(author);
        given(authorMapper.toDto(author)).willReturn(authorResDTO);

        authorService.update(1L, authorReqDTO);

        Mockito.verify(authorRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(authorMapper, Mockito.times(1)).fromDto(authorReqDTO);
        Mockito.verify(authorRepository, Mockito.times(1)).save(author);
        Mockito.verify(authorMapper, Mockito.times(1)).toDto(author);

    }

    @DisplayName("не изменить автора из-за нулевого идентификатора")
    @Test
    void updateAuthorZeroIdError() {

        AuthorReqDTO authorReqDTO = new AuthorReqDTO("Фамилия", "Имя", "Отчество");

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.update(0, authorReqDTO);

        });

    }

    @DisplayName("не изменить автора из-за несуществующего идентификатора")
    @Test
    void updateAuthorInvalidIdError() {

        AuthorReqDTO authorReqDTO = new AuthorReqDTO("Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.update(1, authorReqDTO);

        });

    }

    @DisplayName("вернуть существующего автора по переданному идентификатору")
    @Test
    void getById() {

        AuthorResDTO authorResDTO = new AuthorResDTO(1L,"Фамилия", "Имя", "Отчество");

        Author foundAuthor = new Author(1L,"Фамилия", "Имя", "Отчество");

        given(authorRepository.findById(1L)).willReturn(Optional.of(foundAuthor));
        given(authorMapper.toDto(foundAuthor)).willReturn(authorResDTO);

        authorService.getById(1L);

        Mockito.verify(authorRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(authorMapper, Mockito.times(1)).toDto(foundAuthor);

    }

    @DisplayName("не вернуть автора из-за нулевого идентификатора")
    @Test
    void getByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.getById(0);

        });

    }

    @DisplayName("не вернуть автора из-за несуществующего идентификатора")
    @Test
    void getByIdInvalidIdError() {

        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.getById(1L);

        });

    }

    @DisplayName("удалить автора по переданному идентификатору")
    @Test
    void deleteById() {

        Author author = new Author(1L, "Фамилия", "Имя", "Отчество");

        BooleanExpression predicate = QBook.book.authors.any().id.eq(1L);

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(bookRepository.findAll(predicate)).willReturn(null);
        Mockito.doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteById(1L);

        Mockito.verify(authorRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).findAll(predicate);
        Mockito.verify(authorRepository, Mockito.times(1)).deleteById(1L);

    }

    @DisplayName("не удалить автора из-за нулевого идентификатора")
    @Test
    void deleteByIdZeroIdError() {

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.deleteById(0);

        });

    }

    @DisplayName("не удалить автора из-за наличия связи с книгами")
    @Test
    void deleteByIdLinkedBookCountError() {

        BooleanExpression predicate = QBook.book.authors.any().id.eq(1L);

        List<Book> books = new ArrayList<>();
        books.add(new Book());

        given(bookRepository.findAll(predicate)).willReturn(books);

        Assertions.assertThrows(BusinessException.class, () -> {

            authorService.deleteById(1L);

        });

    }

    @DisplayName("найдёт авторов по параметрам")
    @Test
    void findByParams() {

        AuthorResListDTO authorResListDTO = new AuthorResListDTO(1L,"Фамилия", "Имя", "Отчество");
        List<AuthorResListDTO> authorsResListDTO = new ArrayList<>();
        authorsResListDTO.add(authorResListDTO);

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        BooleanExpression predicate = QAuthor.author.lastName.containsIgnoreCase("Фамилия")
                .and(QAuthor.author.firstName.containsIgnoreCase("Имя"))
                .and(QAuthor.author.middleName.containsIgnoreCase("Отчество"));

        given(authorRepository.findAll(predicate)).willReturn(authors);
        given(authorMapper.toListDto(authors)).willReturn(authorsResListDTO);

        authorService.findByParams("Фамилия", "Имя", "Отчество");

        Mockito.verify(authorRepository, Mockito.times(1)).findAll(predicate);
        Mockito.verify(authorMapper, Mockito.times(1)).toListDto(authors);

    }

    @DisplayName("вернёт всех авторов")
    @Test
    void findAll() {

        AuthorResListDTO authorResListDTO = new AuthorResListDTO(1L,"Фамилия", "Имя", "Отчество");
        List<AuthorResListDTO> authorsResListDTO = new ArrayList<>();
        authorsResListDTO.add(authorResListDTO);

        Author author = new Author(1L,"Фамилия", "Имя", "Отчество");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        given(authorRepository.findAll()).willReturn(authors);
        given(authorMapper.toListDto(authors)).willReturn(authorsResListDTO);

        authorService.findAll();

        Mockito.verify(authorRepository, Mockito.times(1)).findAll();
        Mockito.verify(authorMapper, Mockito.times(1)).toListDto(authors);

    }

}
