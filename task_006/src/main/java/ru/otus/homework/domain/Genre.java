package ru.otus.homework.domain;

import lombok.Data;

@Data
public class Genre {
    private long id;
    private String name;
    private String description;

    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Genre(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
