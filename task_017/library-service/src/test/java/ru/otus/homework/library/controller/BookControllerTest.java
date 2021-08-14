package ru.otus.homework.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.homework.library.dto.*;
import ru.otus.homework.library.dto.*;
import ru.otus.homework.library.service.BookService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Контроллер для книг должен")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    private AuthorReqIdDTO createAuthorReqIdDTO(Long id) {
        AuthorReqIdDTO authorReqIdDTO = new AuthorReqIdDTO();
        authorReqIdDTO.setId(id);
        return authorReqIdDTO;
    }

    private GenreReqIdDTO createGenreReqIdDTO(Long id) {
        GenreReqIdDTO genreReqIdDTO = new GenreReqIdDTO();
        genreReqIdDTO.setId(id);
        return genreReqIdDTO;
    }

    private BookReqDTO createBookReqDTO(String title, String isbn, String description, List<AuthorReqIdDTO> authorsReqIdDTO, List<GenreReqIdDTO> genresReqIdDTO) {
        BookReqDTO bookReqDTO = new BookReqDTO();
        bookReqDTO.setTitle(title);
        bookReqDTO.setIsbn(isbn);
        bookReqDTO.setDescription(description);
        bookReqDTO.setAuthors(authorsReqIdDTO);
        bookReqDTO.setGenres(genresReqIdDTO);
        return bookReqDTO;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить всех авторов")
    @Test
    public void readAll() throws Exception {

        this.mockMvc.perform(get("/book/books/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Весь Шерлок Холмс")))
                .andExpect(content().string(containsString("978-5-17-105207-2")))
                .andExpect(content().string(containsString("Повесть о настоящем человеке")))
                .andExpect(content().string(containsString("978-5-17-064314-1")))
                .andExpect(content().string(containsString("Сказка о рыбаке и рыбке")))
                .andExpect(content().string(containsString("978-5-353-08602-4")))
                .andExpect(content().string(containsString("Русские волшебные сказки")))
                .andExpect(content().string(containsString("978-5-00108-639-0")))
                .andExpect(content().string(containsString("Дойл")))
                .andExpect(content().string(containsString("Артур")))
                .andExpect(content().string(containsString("Конан")))
                .andExpect(content().string(containsString("Полевой")))
                .andExpect(content().string(containsString("Борис")))
                .andExpect(content().string(containsString("Николаевич")))
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
                .andExpect(content().string(containsString("Жуковский")))
                .andExpect(content().string(containsString("Василий")))
                .andExpect(content().string(containsString("Андреевич")))
                .andExpect(content().string(containsString("Аксаков")))
                .andExpect(content().string(containsString("Сергей")))
                .andExpect(content().string(containsString("Тимофеевич")))
                .andExpect(content().string(containsString("Детектив")))
                .andExpect(content().string(containsString("Повесть")))
                .andExpect(content().string(containsString("Сказка")))
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все книги по названию")
    @Test
    public void findByTitle() throws Exception {

        this.mockMvc.perform(get("/book/books?bookTitle=Сказка о рыбаке и рыбке"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сказка о рыбаке и рыбке")))
                .andExpect(content().string(containsString("978-5-353-08602-4")))
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
                .andExpect(content().string(containsString("Сказка")))
        ;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все книги по ISBN")
    @Test
    public void findByISBN() throws Exception {

        this.mockMvc.perform(get("/book/books?bookISBN=353"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сказка о рыбаке и рыбке")))
                .andExpect(content().string(containsString("978-5-353-08602-4")))
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
                .andExpect(content().string(containsString("Сказка")))
        ;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все книги по идентификатору автора")
    @Test
    public void findByAuthorId() throws Exception {

        this.mockMvc.perform(get("/book/books?bookAuthorId=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сказка о рыбаке и рыбке")))
                .andExpect(content().string(containsString("978-5-353-08602-4")))
                .andExpect(content().string(containsString("Русские волшебные сказки")))
                .andExpect(content().string(containsString("978-5-00108-639-0")))
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
                .andExpect(content().string(containsString("Жуковский")))
                .andExpect(content().string(containsString("Василий")))
                .andExpect(content().string(containsString("Андреевич")))
                .andExpect(content().string(containsString("Аксаков")))
                .andExpect(content().string(containsString("Сергей")))
                .andExpect(content().string(containsString("Тимофеевич")))
                .andExpect(content().string(containsString("Сказка")))
        ;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все книги по идентификатору жанра")
    @Test
    public void findByGenreId() throws Exception {

        this.mockMvc.perform(get("/book/books?bookGenreId=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сказка о рыбаке и рыбке")))
                .andExpect(content().string(containsString("978-5-353-08602-4")))
                .andExpect(content().string(containsString("Русские волшебные сказки")))
                .andExpect(content().string(containsString("978-5-00108-639-0")))
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
                .andExpect(content().string(containsString("Жуковский")))
                .andExpect(content().string(containsString("Василий")))
                .andExpect(content().string(containsString("Андреевич")))
                .andExpect(content().string(containsString("Аксаков")))
                .andExpect(content().string(containsString("Сергей")))
                .andExpect(content().string(containsString("Тимофеевич")))
                .andExpect(content().string(containsString("Сказка")))
        ;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все книги по параметрам")
    @Test
    public void findByParam() throws Exception {

        this.mockMvc.perform(get("/book/books?bookTitle=Сказка о рыбаке и рыбке&bookISBN=353&bookAuthorId=3&bookGenreId=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сказка о рыбаке и рыбке")))
                .andExpect(content().string(containsString("978-5-353-08602-4")))
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
                .andExpect(content().string(containsString("Сказка")))
        ;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить книгу по идентификатору")
    @Test
    public void read() throws Exception {

        this.mockMvc.perform(get("/book/books/" + 3))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сказка о рыбаке и рыбке")))
                .andExpect(content().string(containsString("978-5-353-08602-4")))
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
                .andExpect(content().string(containsString("Сказка")))
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать книгу")
    @Test
    public void create() throws Exception {

        AuthorReqIdDTO authorDTO = createAuthorReqIdDTO(1L);
        GenreReqIdDTO genreDTO = createGenreReqIdDTO(2L);

        BookReqDTO bookReqDTO = createBookReqDTO("Название", "ISBN", "Описание", List.of(authorDTO), List.of(genreDTO));

        String json = new ObjectMapper().writeValueAsString(bookReqDTO);

        mockMvc.perform(post("/book/books").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        List<BookResListDTO> books = bookService.findByParams(null, "ISBN", null, null);
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getAuthors().size()).isEqualTo(1);
        assertThat(books.get(0).getAuthors().get(0).getId()).isEqualTo(1);
        assertThat(books.get(0).getGenres().get(0).getId()).isEqualTo(2);

        if(books != null && books.size() == 1) {
            Long id = books.get(0).getId();
            bookService.deleteById(id);
        }

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующую книгу")
    @Test
    public void update() throws Exception {

        AuthorReqIdDTO authorDTO = createAuthorReqIdDTO(1L);
        GenreReqIdDTO genreDTO = createGenreReqIdDTO(2L);

        BookReqDTO bookReqDTO = createBookReqDTO("Название", "ISBN", "Описание", List.of(authorDTO), List.of(genreDTO));
        BookResDTO bookResDTO = bookService.add(bookReqDTO);

        AuthorReqIdDTO updateAuthorDTO = createAuthorReqIdDTO(1L);
        GenreReqIdDTO updateGenreDTO = createGenreReqIdDTO(2L);

        BookReqDTO updateBookReqDTO = createBookReqDTO("Название 1", "ISBN 1", "Описание 1", List.of(updateAuthorDTO), List.of(updateGenreDTO));

        String json = new ObjectMapper().writeValueAsString(updateBookReqDTO);

        this.mockMvc.perform(put("/book/books/" + bookResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookResDTO.getId()))
                .andExpect(content().string(containsString("Название 1")))
                .andExpect(content().string(containsString("ISBN 1")))
                .andExpect(content().string(containsString("Описание 1")))
                .andExpect(content().string(containsString("Дойл")))
                .andExpect(content().string(containsString("Артур")))
                .andExpect(content().string(containsString("Конан")))
                .andExpect(content().string(containsString("Повесть")))
        ;

        bookService.deleteById(bookResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующую книгу")
    @Test
    public void delete() throws Exception {

        AuthorReqIdDTO authorDTO = createAuthorReqIdDTO(1L);
        GenreReqIdDTO genreDTO = createGenreReqIdDTO(2L);

        BookReqDTO bookReqDTO = createBookReqDTO("Название", "ISBN", "Описание", List.of(authorDTO), List.of(genreDTO));
        BookResDTO bookResDTO = bookService.add(bookReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/book/books/" + bookResDTO.getId()))
                .andDo(print());

        List<BookResListDTO> books = bookService.findByParams(null, "ISBN", null, null);

        assertThat(books.size()).isEqualTo(0);

    }

}
