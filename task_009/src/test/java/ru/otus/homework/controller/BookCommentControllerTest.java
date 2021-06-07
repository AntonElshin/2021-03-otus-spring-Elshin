package ru.otus.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.homework.dto.BookCommentResDTO;
import ru.otus.homework.dto.BookDTO;
import ru.otus.homework.dto.BookCommentDTO;
import ru.otus.homework.service.BookCommentService;

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

    @DisplayName("получить все комментарии по идентификатору книги")
    @Test
    public void findByBookId() throws Exception {

        this.mockMvc.perform(get("/api/bookcomments?bookId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Увлекательный детектив!")))
                .andExpect(content().string(containsString("Неожиданная концовка!")))
        ;
    }

    @DisplayName("получить комментарий к книге по идентификатору")
    @Test
    public void read() throws Exception {

        this.mockMvc.perform(get("/api/bookcomments/" + 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Увлекательный детектив!")))
        ;

    }

    @DisplayName("создать комментарий к книге")
    @Test
    public void create() throws Exception {

        BookDTO bookDTO = new BookDTO(3L, null, null, null, null, null);
        BookCommentDTO bookCommentDTO = new BookCommentDTO(bookDTO, "Грустная");

        String json = new ObjectMapper().writeValueAsString(bookCommentDTO);

        mockMvc.perform(post("/api/bookcomments").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        List<BookCommentResDTO> bookComments = bookCommentService.findAllByBookId(3L);
        assertThat(bookComments.size()).isEqualTo(1);

        if(bookComments != null && bookComments.size() == 1) {
            Long id = bookComments.get(0).getId();
            bookCommentService.deleteById(id);
        }

    }

    @DisplayName("обновить существующий комментарий к книге")
    @Test
    public void update() throws Exception {

        BookDTO bookDTO = new BookDTO(3L, null, null, null, null, null);
        BookCommentDTO bookCommentDTO = new BookCommentDTO(bookDTO, "Грустная");

        BookCommentResDTO createdBookCommentDTO = bookCommentService.add(bookCommentDTO);

        BookDTO updateBookDTO = new BookDTO(3L, null, null, null, null, null);
        BookCommentDTO updateBookCommentDTO = new BookCommentDTO(updateBookDTO, "Грустная!!");

        String json = new ObjectMapper().writeValueAsString(updateBookCommentDTO);

        this.mockMvc.perform(put("/api/bookcomments/" + createdBookCommentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdBookCommentDTO.getId()))
                .andExpect(content().string(containsString("Грустная!!")))
        ;

        bookCommentService.deleteById(createdBookCommentDTO.getId());

    }

    @DisplayName("удалить существующий комментарий к книге")
    @Test
    public void delete() throws Exception {

        BookDTO bookDTO = new BookDTO(3L, null, null, null, null, null);
        BookCommentDTO bookCommentDTO = new BookCommentDTO(bookDTO, "Грустная");

        BookCommentResDTO createdBookCommentDTO = bookCommentService.add(bookCommentDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/bookcomments/" + createdBookCommentDTO.getId()))
                .andDo(print());

        List<BookCommentResDTO> bookComments = bookCommentService.findAllByBookId(3L);

        assertThat(bookComments.size()).isEqualTo(0);

    }

}
