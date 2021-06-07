package ru.otus.homework.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.dto.AuthorDTO;
import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.dto.BookDTO;
import ru.otus.homework.dto.GenreDTO;
import ru.otus.homework.exceptions.BusinessException;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Service для валидации DTO должен")
@ExtendWith(MockitoExtension.class)
public class ValidationServiceTest {

    @InjectMocks
    private ValidationService validationService;

    private GenreDTO createGenreDTO() {
        GenreDTO genreDTO = new GenreDTO(1L, "Жанр", "Описание");
        return genreDTO;
    }

    private AuthorDTO createAuthorDTO() {
        AuthorDTO authorDTO = new AuthorDTO(1L, "Елшин", "Антон", "Николаевич");
        return authorDTO;
    }

    private BookDTO createBookDTO() {
        BookDTO bookDTO = new BookDTO(1L, "Название", "ISBN", "Описание", List.of(createAuthorDTO()), List.of(createGenreDTO()));
        return bookDTO;
    }

    private BookCommentDTO createBookCommentDTO() {
        BookCommentDTO bookCommentDTO = new BookCommentDTO(1L, createBookDTO(), "Комментарий");
        return bookCommentDTO;
    }

    //Жанры

    @DisplayName("успешно свалидировать GenreDTO")
    @Test
    void validateGenreDTO() {
        GenreDTO genreDTO = createGenreDTO();
        validationService.validateDTO(genreDTO);
    }

    @DisplayName("успешно свалидировать GenreDTO без идентификатора и описания")
    @Test
    void validateGenreDTOWithoutIdAndDescription() {
        GenreDTO genreDTO = createGenreDTO();
        genreDTO.setId(null);
        genreDTO.setDescription(null);
        validationService.validateDTO(genreDTO);
    }

    @DisplayName("не свалидировать GenreDTO из-за null в названии")
    @Test
    void validateGenreDTONullNameError() {

        GenreDTO genreDTO = createGenreDTO();
        genreDTO.setName(null);

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(genreDTO);
        });

    }

    @DisplayName("не свалидировать GenreDTO из-за пустой строки в названии")
    @Test
    void validateGenreDTOEmptyNameError() {

        GenreDTO genreDTO = createGenreDTO();
        genreDTO.setName("");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(genreDTO);
        });

    }

    @DisplayName("не свалидировать GenreDTO из-за null в названии без учёта пробелов")
    @Test
    void validateGenreDTOEmptyNameIgnoreSpacesError() {

        GenreDTO genreDTO = createGenreDTO();
        genreDTO.setName("  ");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(genreDTO);
        });

    }

    //Авторы

    @DisplayName("успешно свалидировать AuthorDTO")
    @Test
    void validateAuthorDTO() {
        AuthorDTO authorDTO = createAuthorDTO();
        validationService.validateDTO(authorDTO);
    }

    @DisplayName("успешно свалидировать AuthorDTO без идентификатора и отчества")
    @Test
    void validateAuthorDTOWithoutIdAndMiddleName() {
        AuthorDTO authorDTO = createAuthorDTO();
        authorDTO.setId(null);
        authorDTO.setMiddleName(null);
        validationService.validateDTO(authorDTO);
    }

    @DisplayName("не свалидировать AuthorDTO из-за null в фамилии")
    @Test
    void validateAuthorDTONullLastNameError() {

        AuthorDTO authorDTO = createAuthorDTO();
        authorDTO.setLastName(null);

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(authorDTO);
        });

    }

    @DisplayName("не свалидировать AuthorDTO из-за пустой строки в фамилии")
    @Test
    void validateAuthorDTOEmptyLastNameError() {

        AuthorDTO authorDTO = createAuthorDTO();
        authorDTO.setLastName("");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(authorDTO);
        });

    }

    @DisplayName("не свалидировать AuthorDTO из-за пустой строки в фамилии без учёта пробелов")
    @Test
    void validateAuthorDTOEmptyLastNameIgnoreSpacesError() {

        AuthorDTO authorDTO = createAuthorDTO();
        authorDTO.setLastName("  ");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(authorDTO);
        });

    }

    @DisplayName("не свалидировать AuthorDTO из-за null в имени")
    @Test
    void validateAuthorDTONullFirstNameError() {

        AuthorDTO authorDTO = createAuthorDTO();
        authorDTO.setFirstName(null);

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(authorDTO);
        });

    }

    @DisplayName("не свалидировать AuthorDTO из-за пустой строки в имени")
    @Test
    void validateAuthorDTOEmptyFirstNameError() {

        AuthorDTO authorDTO = createAuthorDTO();
        authorDTO.setLastName("");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(authorDTO);
        });

    }

    @DisplayName("не свалидировать AuthorDTO из-за пустой строки в имени без учёта пробелов")
    @Test
    void validateAuthorDTOEmptyFirstNameIgnoreSpacesError() {

        AuthorDTO authorDTO = createAuthorDTO();
        authorDTO.setLastName("  ");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(authorDTO);
        });

    }

    //Книги

    @DisplayName("успешно свалидировать BookDTO")
    @Test
    void validateBookDTO() {
        BookDTO bookDTO = createBookDTO();
        validationService.validateDTO(bookDTO);
    }

    @DisplayName("успешно свалидировать BookDTO без идентификатора, ISBN, идентификатора")
    @Test
    void validateBookDTOWithoutId_ISBN_Description() {
        BookDTO bookDTO = createBookDTO();
        bookDTO.setId(null);
        bookDTO.setIsbn(null);
        bookDTO.setDescription(null);
        validationService.validateDTO(bookDTO);
    }

    @DisplayName("не свалидировать BookDTO из-за null в названии")
    @Test
    void validateBookDTONullTitleError() {

        BookDTO bookDTO = createBookDTO();
        bookDTO.setTitle(null);

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookDTO);
        });

    }

    @DisplayName("не свалидировать BookDTO из-за пустой строки в названии")
    @Test
    void validateBookDTOEmptyTitleError() {

        BookDTO bookDTO = createBookDTO();
        bookDTO.setTitle("");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookDTO);
        });

    }

    @DisplayName("не свалидировать BookDTO из-за пустой строки в названии без учёта пробелов")
    @Test
    void validateBookDTOEmptyTitleIgnoreSpacesError() {

        BookDTO bookDTO = createBookDTO();
        bookDTO.setTitle("  ");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookDTO);
        });

    }

    @DisplayName("не свалидировать BookDTO из-за отсутствия авторов")
    @Test
    void validateBookDTOAuthorsError() {

        BookDTO bookDTO = createBookDTO();
        bookDTO.setAuthors(new ArrayList<>());

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookDTO);
        });

    }

    @DisplayName("не свалидировать BookDTO из-за отсутствия жанров")
    @Test
    void validateBookDTOGenresError() {

        BookDTO bookDTO = createBookDTO();
        bookDTO.setGenres(new ArrayList<>());

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookDTO);
        });

    }

    //Комментарий к книге

    @DisplayName("успешно свалидировать BookCommentDTO")
    @Test
    void validateBookCommentDTO() {
        BookCommentDTO bookCommentDTO = createBookCommentDTO();
        validationService.validateDTO(bookCommentDTO);
    }

    @DisplayName("успешно свалидировать BookCommentDTO без идентификатора")
    @Test
    void validateBookCommentDTOWithoutId() {
        BookCommentDTO bookCommentDTO = createBookCommentDTO();
        bookCommentDTO.setId(null);
        validationService.validateDTO(bookCommentDTO);
    }

    @DisplayName("не свалидировать BookDTO из-за null в книге")
    @Test
    void validateBookCommentDTONullBookError() {

        BookCommentDTO bookCommentDTO = createBookCommentDTO();
        bookCommentDTO.setBook(null);

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookCommentDTO);
        });

    }

    @DisplayName("не свалидировать BookDTO из-за null в комментарии")
    @Test
    void validateBookCommentDTONullTextError() {

        BookCommentDTO bookCommentDTO = createBookCommentDTO();
        bookCommentDTO.setText(null);

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookCommentDTO);
        });

    }

    @DisplayName("не свалидировать BookDTO из-за пустой строки в комментарии")
    @Test
    void validateBookCommentDTOEmptyTextError() {

        BookCommentDTO bookCommentDTO = createBookCommentDTO();
        bookCommentDTO.setText("");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookCommentDTO);
        });

    }

    @DisplayName("не свалидировать BookDTO из-за пустой строки в комментарии без учёта пробелов")
    @Test
    void validateBookCommentDTOEmptyTextIgnoreSpacesError() {

        BookCommentDTO bookCommentDTO = createBookCommentDTO();
        bookCommentDTO.setText("  ");

        Assertions.assertThrows(BusinessException.class, () -> {
            validationService.validateDTO(bookCommentDTO);
        });

    }

}
