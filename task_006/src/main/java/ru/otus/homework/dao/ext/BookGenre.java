package ru.otus.homework.dao.ext;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.otus.homework.domain.Genre;

@Data
@RequiredArgsConstructor
public class BookGenre {
    private final long bookId;
    private final Genre genre;
}
