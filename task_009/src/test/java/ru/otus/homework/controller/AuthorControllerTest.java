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
import ru.otus.homework.dto.AuthorReqDTO;
import ru.otus.homework.dto.AuthorResDTO;
import ru.otus.homework.dto.AuthorResListDTO;
import ru.otus.homework.service.AuthorService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Контроллер для авторов должен")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorService authorService;

    @DisplayName("получить всех авторов")
    @Test
    public void readAll() throws Exception {

        this.mockMvc.perform(get("/api/authors/all"))
                .andDo(print())
                .andExpect(status().isOk())
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
        ;

    }

    @DisplayName("получить всех авторов по фамилии")
    @Test
    public void findByLastName() throws Exception {

        this.mockMvc.perform(get("/api/authors?lastName=Пушкин"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
        ;
    }

    @DisplayName("получить всех авторов по имени")
    @Test
    public void findByFirstName() throws Exception {

        this.mockMvc.perform(get("/api/authors?firstName=Александр"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
        ;
    }

    @DisplayName("получить всех авторов по отчеству")
    @Test
    public void findByMiddleName() throws Exception {

        this.mockMvc.perform(get("/api/authors?middleName=Сергеевич"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
        ;
    }

    @DisplayName("получить всех авторов по фамилии")
    @Test
    public void findByLastNameAndFirstNameAndMiddleName() throws Exception {

        this.mockMvc.perform(get("/api/authors?lastName=Пушкин&firstName=Александр&middleName=Сергеевич"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Пушкин")))
                .andExpect(content().string(containsString("Александр")))
                .andExpect(content().string(containsString("Сергеевич")))
        ;
    }

    @DisplayName("получить автора по идентификатору")
    @Test
    public void read() throws Exception {

        this.mockMvc.perform(get("/api/authors/" + 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Дойл")))
                .andExpect(content().string(containsString("Артур")))
                .andExpect(content().string(containsString("Конан")))
        ;

    }

    @DisplayName("создать автора")
    @Test
    public void create() throws Exception {

        AuthorReqDTO authorReqDTO = new AuthorReqDTO("Елшин", "Антон", "Николаевич");

        String json = new ObjectMapper().writeValueAsString(authorReqDTO);

        mockMvc.perform(post("/api/authors").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        List<AuthorResListDTO> authors = authorService.findByParams("Елшин", "Антон", "Николаевич");
        assertThat(authors.size()).isEqualTo(1);
        if(authors != null && authors.size() == 1) {
            Long id = authors.get(0).getId();
            authorService.deleteById(id);
        }

    }

    @DisplayName("обновить существующего автора")
    @Test
    public void update() throws Exception {

        AuthorReqDTO authorReqDTO = new AuthorReqDTO("Елшин", "Антон", "Николаевич");
        AuthorResDTO authorResDTO = authorService.add(authorReqDTO);

        AuthorReqDTO updateAuthorReqDTO = new AuthorReqDTO("Елшин 1", "Антон 1", "Николаевич 1");

        String json = new ObjectMapper().writeValueAsString(updateAuthorReqDTO);

        this.mockMvc.perform(put("/api/authors/" + authorResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorResDTO.getId()))
                .andExpect(content().string(containsString("Елшин 1")))
                .andExpect(content().string(containsString("Антон 1")))
                .andExpect(content().string(containsString("Николаевич 1")))
        ;

        authorService.deleteById(authorResDTO.getId());

    }

    @DisplayName("удалить существующего автора")
    @Test
    public void delete() throws Exception {

        AuthorReqDTO authorReqDTO = new AuthorReqDTO("Елшин", "Антон", "Николаевич");
        AuthorResDTO authorResDTO = authorService.add(authorReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/authors/" + authorResDTO.getId()))
                .andDo(print());

        List<AuthorResListDTO> authors = authorService.findByParams("Елшин", "Антон", "Николаевич");

        assertThat(authors.size()).isEqualTo(0);

    }

}
