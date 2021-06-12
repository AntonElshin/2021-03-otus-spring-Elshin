package ru.otus.homework.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс с вспомогательными функциями")
public class UtilsTest {

    @DisplayName("получит уникальные идентификаторы")
    @Test
    void getUniqueIds() {

        List<Long> ids = Utils.getUniqueIds("1,2");

        assertThat(ids).hasSize(2);
        assertThat(ids.get(0)).isEqualTo(1L);
        assertThat(ids.get(1)).isEqualTo(2L);

    }

    @DisplayName("получит уникальные идентификаторы без учёта пробелов")
    @Test
    void getUniqueIdsIgnoreSpaces() {

        List<Long> ids = Utils.getUniqueIds(" 1 , 2 ");

        assertThat(ids).hasSize(2);
        assertThat(ids.get(0)).isEqualTo(1L);
        assertThat(ids.get(1)).isEqualTo(2L);

    }

    @DisplayName("получит уникальные идентификаторы без учёта пробелов")
    @Test
    void getUniqueIdsIgnoreDuplication() {

        List<Long> ids = Utils.getUniqueIds("1,2,2,1");

        assertThat(ids).hasSize(2);
        assertThat(ids.get(0)).isEqualTo(1L);
        assertThat(ids.get(1)).isEqualTo(2L);

    }

}
