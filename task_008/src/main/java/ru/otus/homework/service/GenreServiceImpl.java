package ru.otus.homework.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.QBook;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final PrintService printService;

    @Override
    @Transactional
    public Genre add(String name, String description) {

        if(name == null || (name != null && name.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_NAME);
        }

        List<Genre> genres = genreRepository.findByNameEqualsIgnoreCase(name);

        if(genres != null && genres.size() > 0) {
            throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, name);
        }

        Genre genre = new Genre(name, description);

        genreRepository.save(genre);
        return genre;
    }

    @Override
    @Transactional
    public void update(long id, String name, String description) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Optional<Genre> foundGenre = genreRepository.findById(id);

        if(!foundGenre.isPresent()) {
            throw new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id);
        }

        Genre genre = foundGenre.get();

        if(name != null && !name.trim().isEmpty()) {
            if (!genre.getName().equalsIgnoreCase(name)) {
                List<Genre> genres = genreRepository.findByNameEqualsIgnoreCase(name);
                if (genres != null && genres.size() > 0) {
                    throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, name);
                }
            }
        }

        if(name != null && !name.trim().isEmpty()) {
            genre.setName(name);
        }
        if(description != null && !description.trim().isEmpty()) {
            genre.setDescription(description);
        }

        genreRepository.save(genre);

    }

    @Override
    @Transactional(readOnly = true)
    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Optional<Genre> foundGenre = genreRepository.findById(id);

        if(!foundGenre.isPresent()) {
            throw new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id);
        }

        printService.printGenre(foundGenre.get());

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        List<Book> books = bookRepository.findAll(QBook.book.genres.any().id.eq(id));

        if(books != null && books.size() > 0) {
            throw new BusinessException(Errors.GENRE_HAS_LINKED_BOOKS);
        }

        genreRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public void findByParams(String name) {
        List<Genre> genres = genreRepository.findByNameContainingIgnoreCase(name);
        printService.printGenres(genres);
    }

    @Override
    @Transactional(readOnly = true)
    public void findAll() {
        List<Genre> genres = genreRepository.findAll();
        printService.printGenres(genres);
    }

    @Override
    @Transactional(readOnly = true)
    public void count() {
        printService.printGenresCount(genreRepository.count());
    }

}
