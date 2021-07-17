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
import ru.otus.homework.library.dto.GenreReqDTO;
import ru.otus.homework.library.dto.GenreResDTO;
import ru.otus.homework.library.dto.GenreResListDTO;
import ru.otus.homework.library.service.GenreService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Контроллер для жанров должен")
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreService genreService;

    private GenreReqDTO createGenreReqDTO(String name, String description) {
        GenreReqDTO genreReqDTO = new GenreReqDTO();
        genreReqDTO.setName(name);
        genreReqDTO.setDescription(description);
        return genreReqDTO;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все жанры")
    @Test
    public void readAll() throws Exception {

        this.mockMvc.perform(get("/genre/genres/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Детектив")))
                .andExpect(content().string(containsString("Повесть")))
                .andExpect(content().string(containsString("Сказка")))
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить все жанры по названию")
    @Test
    public void findByName() throws Exception {

        this.mockMvc.perform(get("/genre/genres?genreName=Детектив"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Детектив")))
        ;
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("получить жанр по идентификатору")
    @Test
    public void read() throws Exception {

        // получаем по идентификатору
        this.mockMvc.perform(get("/genre/genres/" + 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Детектив")))
                .andExpect(content().string(containsString("Преимущественно")))
        ;

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать жанр")
    @Test
    public void create() throws Exception {

        GenreReqDTO genreReqDTO = createGenreReqDTO("Жанр", "Описание");

        String json = new ObjectMapper().writeValueAsString(genreReqDTO);

        mockMvc.perform(post("/genre/genres").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        List<GenreResListDTO> genres = genreService.findByParams("Жанр");
        assertThat(genres.size()).isEqualTo(1);
        if(genres != null && genres.size() == 1) {
            Long id = genres.get(0).getId();
            genreService.deleteById(id);
        }

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить существующий жанр")
    @Test
    public void update() throws Exception {

        GenreReqDTO genreReqDTO = createGenreReqDTO("Жанр", "Описание");
        GenreResDTO genreResDTO = genreService.add(genreReqDTO);

        GenreReqDTO updateGenreReqDTO = createGenreReqDTO("Жанр 1", "Описание 1");

        String json = new ObjectMapper().writeValueAsString(updateGenreReqDTO);

        this.mockMvc.perform(put("/genre/genres/" + genreResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(genreResDTO.getId()))
                .andExpect(content().string(containsString("Жанр 1")))
                .andExpect(content().string(containsString("Описание 1")))
        ;

        genreService.deleteById(genreResDTO.getId());

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалить существующий жанр")
    @Test
    public void delete() throws Exception {

        GenreReqDTO genreReqDTO = createGenreReqDTO("Жанр", "Описание");
        GenreResDTO genreResDTO = genreService.add(genreReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/genre/genres/" + genreResDTO.getId()))
                .andDo(print());

        List<GenreResListDTO> authors = genreService.findByParams("Жанр");

        assertThat(authors.size()).isEqualTo(0);

    }

}
