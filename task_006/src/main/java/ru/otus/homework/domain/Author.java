package ru.otus.homework.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class Author {
    private long id;
    private String lastName;
    private String firstName;
    private String middleName;

    public Author(String lastName, String firstName, String middleName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }

    public Author(long id, String lastName, String firstName, String middleName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }
}
