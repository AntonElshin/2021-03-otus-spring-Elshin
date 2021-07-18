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
import ru.otus.homework.library.dto.BookCommentResDTO;
import ru.otus.homework.library.dto.BookCommentReqDTO;
import ru.otus.homework.library.dto.BookCommentResListDTO;
import ru.otus.homework.library.dto.BookReqIdDTO;
import ru.otus.homework.library.service.BookCommentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Контроллер для комментариев к книгам должен")
public class BookCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookCommentService bookCommentService;

    private BookReqIdDTO createBookReqIdDTO(Long id) {
        BookReqIdDTO bookReqIdDTO = new BookReqIdDTO();
        bookReqIdDTO.setId(id);
        return bookReqIdDTO;
    }

    private BookCommentReqDTO createBookCommentReqDTO(BookReqIdDTO bookReqIdDTO, String text) {
        BookCommentReqDTO bookCommentReqDTO = new BookCommentReqDTO();
        bookCommentReqDTO.setBook(bookReqIdDTO);
        bookCommentReqDTO.setText(text);
        return bookCommentReqDTO;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все комментарии по идентификатору книги")
    @Test
    public void findByBookId() throws Exception {

        this.mockMvc.perform(get("/book-comment/bookcomments?commentBookId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Увлекательный детектив!")))
                .andExpect(content().string(containsString("Неожиданная концовка!")))
        ;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить комментарий к книге по идентификатору")
    @Test
    public void read() throws Exception {

        this.mockMvc.perform(get("/book-comment/bookcomments/" + 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Увлекательный детектив!")))
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать комментарий к книге")
    @Test
    public void create() throws Exception {

        BookReqIdDTO bookReqIdDTO = createBookReqIdDTO(3L);
        BookCommentReqDTO bookCommentReqDTO = createBookCommentReqDTO(bookReqIdDTO, "Грустная");

        String json = new ObjectMapper().writeValueAsString(bookCommentReqDTO);

        mockMvc.perform(post("/book-comment/bookcomments").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        List<BookCommentResListDTO> bookComments = bookCommentService.findAllByBookId(3L);
        assertThat(bookComments.size()).isEqualTo(1);

        if(bookComments != null && bookComments.size() == 1) {
            Long id = bookComments.get(0).getId();
            bookCommentService.deleteById(id);
        }

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующий комментарий к книге")
    @Test
    public void update() throws Exception {

        BookReqIdDTO bookReqIdDTO = createBookReqIdDTO(1L);
        BookCommentReqDTO bookCommentReqDTO = createBookCommentReqDTO(bookReqIdDTO, "Грустная");

        BookCommentResDTO createdBookCommentDTO = bookCommentService.add(bookCommentReqDTO);

        BookReqIdDTO updateBookReqIdDTO = createBookReqIdDTO(1L);
        BookCommentReqDTO updateBookCommentReqDTO = createBookCommentReqDTO(updateBookReqIdDTO, "Грустная!!");

        String json = new ObjectMapper().writeValueAsString(updateBookCommentReqDTO);

        this.mockMvc.perform(put("/book-comment/bookcomments/" + createdBookCommentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdBookCommentDTO.getId()))
                .andExpect(content().string(containsString("Грустная!!")))
        ;

        bookCommentService.deleteById(createdBookCommentDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующий комментарий к книге")
    @Test
    public void delete() throws Exception {

        BookReqIdDTO bookReqIdDTO = createBookReqIdDTO(1L);
        BookCommentReqDTO bookCommentReqDTO = createBookCommentReqDTO(bookReqIdDTO, "Грустная");

        BookCommentResDTO createdBookCommentDTO = bookCommentService.add(bookCommentReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/book-comment/bookcomments/" + createdBookCommentDTO.getId()))
                .andDo(print());

        List<BookCommentResListDTO> bookComments = bookCommentService.findAllByBookId(3L);

        assertThat(bookComments.size()).isEqualTo(0);

    }

}
