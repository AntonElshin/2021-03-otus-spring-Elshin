package ru.otus.homework.dao.ext;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.otus.homework.domain.Author;

@Data
@RequiredArgsConstructor
public class BookAuthor {
    private final long bookId;
    private final Author author;
}
