package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;
    private final PrintService printService;

    public Genre add(String name, String description) {

        if(name == null || (name != null && name.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_NAME);
        }

        List<Genre> genres = genreDao.getByParamsEqualsIgnoreCase(name);

        if(genres != null && genres.size() > 0) {
            throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, name);
        }

        Genre genre = new Genre(name, description);

        genreDao.insert(0, genre);
        return genre;
    }

    public void update(long id, String name, String description) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Genre genre = getGenreById(id);

        if(genre == null) {
            throw new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id);
        }
        if(name != null && !name.trim().isEmpty()) {
            if (!genre.getName().equalsIgnoreCase(name)) {
                List<Genre> genres = genreDao.getByParamsEqualsIgnoreCase(name);
                if (genres != null && genres.size() > 0) {
                    throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, name);
                }
            }
        }

        Boolean changeFlag = false;

        if(name != null && !name.trim().isEmpty()) {
            genre.setName(name);
            changeFlag = true;
        }
        if(description != null && !description.trim().isEmpty()) {
            genre.setDescription(description);
            changeFlag = true;
        }

        if(changeFlag) {
            genreDao.update(id, genre);
        }

    }

    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Genre genre = getGenreById(id);

        if(genre == null) {
            throw new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id);
        }

        printService.printGenre(genre);

    }

    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Genre genre = getGenreById(id);

        if(genre == null) {
            throw new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id);
        }

        if(genreDao.getLinkedBookCount(id) != 0) {
            throw new BusinessException(Errors.GENRE_HAS_LINKED_BOOKS);
        }

        genreDao.deleteById(id);

    }

    public void findByParams(String name) {
        List<Genre> genres = genreDao.getByParamsLikeIgnoreCase(name);
        printService.printGenres(genres);
    }

    public void findAll() {
        List<Genre> genres = genreDao.getAll();
        printService.printGenres(genres);
    }

    public void count() {
        printService.printGenresCount(genreDao.count());
    }

    private Genre getGenreById(long id) {

        Genre genre = null;

        try {
            genre = genreDao.getById(id);
        }
        catch (Exception exception) {
            System.out.println("Жанр не найден по идентификатору: " + id);
        }

        return genre;

    }

}
