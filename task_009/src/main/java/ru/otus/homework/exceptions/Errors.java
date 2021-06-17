package ru.otus.homework.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Errors {
    REQUIRED_PARAM(1, "Missing required param(s): %s"),
    GENRE_WITH_NAME_EXISTS(2, "Genre with name exists: %s"),
    MISSING_REQUIRED_PARAM_GENRE_ID(3, "Missing required param genre id"),
    MISSING_REQUIRED_PARAM_GENRE_NAME(4, "Missing required param genre name"),
    GENRE_NOT_FOUND_BY_ID(5, "Genre not found by id: %s"),
    MISSING_REQUIRED_PARAM_AUTHOR_FIRST_NAME(6, "Missing required param author first name"),
    MISSING_REQUIRED_PARAM_AUTHOR_LAST_NAME(7, "Missing required param author last name"),
    MISSING_REQUIRED_PARAM_AUTHOR_ID(8, "Missing required param author id"),
    AUTHOR_NOT_FOUND_BY_ID(9, "Author not found by id: %s"),
    MISSING_REQUIRED_PARAM_BOOK_TITLE(10, "Missing required param book title"),
    MISSING_REQUIRED_PARAM_AUTHORS(11, "Missing required param authors"),
    MISSING_REQUIRED_PARAM_GENRES(12, "Missing required param genres"),
    BOOK_SHOULD_HAVE_LEAST_ONE_AUTHOR(13, "Book should have least one author"),
    BOOK_SHOULD_HAVE_LEAST_ONE_GENRE(14, "Book should have least one genre"),
    NOT_UNIQUE_BOOK_ISBN(15, "Not unique book isbn"),
    AUTHORS_NOT_FOUND_BY_IDS(16, "Authors not found by ids"),
    GENRES_NOT_FOUND_BY_IDS(17, "Genres not found by ids"),
    MISSING_REQUIRED_PARAM_BOOK_ID(18, "Missing required param book id"),
    BOOK_NOT_FOUND_BY_ID(19, "Book not found by id: %s"),
    AUTHOR_HAS_LINKED_BOOKS(20, "Author has linked books"),
    GENRE_HAS_LINKED_BOOKS(21, "Genre has linked books"),
    MISSING_REQUIRED_PARAM_COMMENT_TEXT(22, "Missing required param comment text"),
    MISSING_REQUIRED_PARAM_COMMENT_ID(23, "Missing required param comment id"),
    BOOK_COMMENT_NOT_FOUND_BY_ID(24, "Book comment not found by id: %s")
    ;

    private Integer code;
    private String message;
}
