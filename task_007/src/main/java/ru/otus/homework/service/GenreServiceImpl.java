package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.BookRepositoryJpa;
import ru.otus.homework.repository.GenreRepositoryJpa;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepositoryJpa genreRepositoryJpa;
    private final BookRepositoryJpa bookRepositoryJpa;
    private final PrintService printService;

    @Override
    @Transactional
    public Genre add(String name, String description) {

        if(name == null || (name != null && name.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_NAME);
        }

        List<Genre> genres = genreRepositoryJpa.findByParamsEqualsIgnoreCase(name);

        if(genres != null && genres.size() > 0) {
            throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, name);
        }

        Genre genre = new Genre(name, description);

        genreRepositoryJpa.save(genre);
        return genre;
    }

    @Override
    @Transactional
    public void update(long id, String name, String description) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Optional<Genre> foundGenre = genreRepositoryJpa.findById(id);

        if(!foundGenre.isPresent()) {
            throw new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id);
        }

        Genre genre = foundGenre.get();

        if(name != null && !name.trim().isEmpty()) {
            if (!genre.getName().equalsIgnoreCase(name)) {
                List<Genre> genres = genreRepositoryJpa.findByParamsEqualsIgnoreCase(name);
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

        genreRepositoryJpa.save(genre);

    }

    @Override
    @Transactional(readOnly = true)
    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Optional<Genre> foundGenre = genreRepositoryJpa.findById(id);

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

        List<Book> books = bookRepositoryJpa.findByParamsEqualsIgnoreCase(null, null, null, id);

        if(books != null && books.size() > 0) {
            throw new BusinessException(Errors.GENRE_HAS_LINKED_BOOKS);
        }

        genreRepositoryJpa.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public void findByParams(String name) {
        List<Genre> genres = genreRepositoryJpa.findByParamsLikeIgnoreCase(name);
        printService.printGenres(genres);
    }

    @Override
    @Transactional(readOnly = true)
    public void findAll() {
        List<Genre> genres = genreRepositoryJpa.findAll();
        printService.printGenres(genres);
    }

    @Override
    @Transactional(readOnly = true)
    public void count() {
        printService.printGenresCount(genreRepositoryJpa.count());
    }

}
