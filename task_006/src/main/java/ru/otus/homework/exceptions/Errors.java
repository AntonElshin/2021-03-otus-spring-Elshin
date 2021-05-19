package ru.otus.homework.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Errors {
    GENRE_WITH_NAME_EXISTS(1, "Genre with name exists: %s"),
    MISSING_REQUIRED_PARAM_GENRE_ID(2, "Missing required param genre id"),
    MISSING_REQUIRED_PARAM_GENRE_NAME(3, "Missing required param genre name"),
    GENRE_NOT_FOUND_BY_ID(4, "Genre not found by id: %s"),
    MISSING_REQUIRED_PARAM_AUTHOR_FIRST_NAME(5, "Missing required param author first name"),
    MISSING_REQUIRED_PARAM_AUTHOR_LAST_NAME(6, "Missing required param author last name"),
    MISSING_REQUIRED_PARAM_AUTHOR_ID(7, "Missing required param author id"),
    AUTHOR_NOT_FOUND_BY_ID(8, "Author not found by id: %s"),
    MISSING_REQUIRED_PARAM_BOOK_TITLE(9, "Missing required param book title"),
    MISSING_REQUIRED_PARAM_AUTHORS(10, "Missing required param authors"),
    MISSING_REQUIRED_PARAM_GENRES(11, "Missing required param genres"),
    BOOK_SHOULD_HAVE_LEAST_ONE_AUTHOR(12, "Book should have least one author"),
    BOOK_SHOULD_HAVE_LEAST_ONE_GENRE(13, "Book should have least one genre"),
    NOT_UNIQUE_BOOK_ISBN(14, "Not unique book isbn"),
    AUTHORS_NOT_FOUND_BY_IDS(15, "Authors not found by ids"),
    GENRES_NOT_FOUND_BY_IDS(16, "Genres not found by ids"),
    MISSING_REQUIRED_PARAM_BOOK_ID(17, "Missing required param book id"),
    BOOK_NOT_FOUND_BY_ID(18, "Book not found by id: %s"),
    AUTHOR_HAS_LINKED_BOOKS(19, "Author has linked books"),
    GENRE_HAS_LINKED_BOOKS(20, "Genre has linked books")
    ;

    private Integer code;
    private String message;
}
