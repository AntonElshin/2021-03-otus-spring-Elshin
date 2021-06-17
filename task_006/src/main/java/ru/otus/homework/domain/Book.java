package ru.otus.homework.domain;

import lombok.Data;

import java.util.List;

@Data
public class Book {
    private long id;
    private String title;
    private String isbn;
    private String description;
    private List<Author> authors;
    private List<Genre> genres;

    public Book(String title, String isbn, String description, List<Author> authors, List<Genre> genres) {
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.authors = authors;
        this.genres = genres;
    }

    public Book(long id, String title, String isbn, String description, List<Author> authors, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.authors = authors;
        this.genres = genres;
    }
}
