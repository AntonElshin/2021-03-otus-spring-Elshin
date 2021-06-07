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
import ru.otus.homework.dto.GenreDTO;
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
                .andExpect(content().string(containsString("Преимущественно")))
                .andExpect(content().string(containsString("Повесть")))
                .andExpect(content().string(containsString("Средняя")))
                .andExpect(content().string(containsString("Сказка")))
                .andExpect(content().string(containsString("в сказках")))
        ;

    }

    @DisplayName("получить все жанры по названию")
    @Test
    public void findByName() throws Exception {

        this.mockMvc.perform(get("/api/genres?name=Детектив"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Детектив")))
                .andExpect(content().string(containsString("Преимущественно")))
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

        GenreDTO genreDTO = new GenreDTO("Жанр", "Описание");

        String json = new ObjectMapper().writeValueAsString(genreDTO);

        mockMvc.perform(post("/api/genres").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        List<GenreDTO> genres = genreService.findByParams("Жанр");
        assertThat(genres.size()).isEqualTo(1);
        if(genres != null && genres.size() == 1) {
            Long id = genres.get(0).getId();
            genreService.deleteById(id);
        }

    }

    @DisplayName("обновить существующий жанр")
    @Test
    public void update() throws Exception {

        GenreDTO genreDTO = new GenreDTO("Жанр", "Описание");
        genreDTO = genreService.add(genreDTO);

        GenreDTO updateGenreDTO = new GenreDTO("Жанр 1", "Описание 1");

        String json = new ObjectMapper().writeValueAsString(updateGenreDTO);

        this.mockMvc.perform(put("/api/genres/" + genreDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(genreDTO.getId()))
                .andExpect(content().string(containsString("Жанр 1")))
                .andExpect(content().string(containsString("Описание 1")))
        ;

        genreService.deleteById(genreDTO.getId());

    }

    @DisplayName("удалить существующий жанр")
    @Test
    public void delete() throws Exception {

        GenreDTO genreDTO = new GenreDTO("Жанр", "Описание");
        genreDTO = genreService.add(genreDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/genres/" + genreDTO.getId()))
                .andDo(print());

        List<GenreDTO> authors = genreService.findByParams("Жанр");

        assertThat(authors.size()).isEqualTo(0);

    }

}
