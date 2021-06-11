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
import ru.otus.homework.dto.GenreReqDTO;
import ru.otus.homework.dto.GenreResDTO;
import ru.otus.homework.dto.GenreResListDTO;
import ru.otus.homework.service.GenreService;

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

    @DisplayName("получить все жанры")
    @Test
    public void readAll() throws Exception {

        this.mockMvc.perform(get("/api/genres/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Детектив")))
                .andExpect(content().string(containsString("Повесть")))
                .andExpect(content().string(containsString("Сказка")))
        ;

    }

    @DisplayName("получить все жанры по названию")
    @Test
    public void findByName() throws Exception {

        this.mockMvc.perform(get("/api/genres?name=Детектив"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Детектив")))
        ;
    }

    @DisplayName("получить жанр по идентификатору")
    @Test
    public void read() throws Exception {

        // получаем по идентификатору
        this.mockMvc.perform(get("/api/genres/" + 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Детектив")))
                .andExpect(content().string(containsString("Преимущественно")))
        ;

    }

    @DisplayName("создать жанр")
    @Test
    public void create() throws Exception {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр", "Описание");

        String json = new ObjectMapper().writeValueAsString(genreReqDTO);

        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        List<GenreResListDTO> genres = genreService.findByParams("Жанр");
        assertThat(genres.size()).isEqualTo(1);
        if(genres != null && genres.size() == 1) {
            Long id = genres.get(0).getId();
            genreService.deleteById(id);
        }

    }

    @DisplayName("обновить существующий жанр")
    @Test
    public void update() throws Exception {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр", "Описание");
        GenreResDTO genreResDTO = genreService.add(genreReqDTO);

        GenreReqDTO updateGenreReqDTO = new GenreReqDTO("Жанр 1", "Описание 1");

        String json = new ObjectMapper().writeValueAsString(updateGenreReqDTO);

        this.mockMvc.perform(put("/api/genres/" + genreResDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(genreResDTO.getId()))
                .andExpect(content().string(containsString("Жанр 1")))
                .andExpect(content().string(containsString("Описание 1")))
        ;

        genreService.deleteById(genreResDTO.getId());

    }

    @DisplayName("удалить существующий жанр")
    @Test
    public void delete() throws Exception {

        GenreReqDTO genreReqDTO = new GenreReqDTO("Жанр", "Описание");
        GenreResDTO genreResDTO = genreService.add(genreReqDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/genres/" + genreResDTO.getId()))
                .andDo(print());

        List<GenreResListDTO> authors = genreService.findByParams("Жанр");

        assertThat(authors.size()).isEqualTo(0);

    }

}
