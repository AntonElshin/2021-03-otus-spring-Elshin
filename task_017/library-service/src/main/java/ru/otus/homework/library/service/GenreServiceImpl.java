package ru.otus.homework.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.library.domain.Book;
import ru.otus.homework.library.domain.Genre;
import ru.otus.homework.library.domain.QBook;
import ru.otus.homework.library.dto.GenreReqDTO;
import ru.otus.homework.library.dto.GenreResDTO;
import ru.otus.homework.library.dto.GenreResListDTO;
import ru.otus.homework.library.exceptions.BusinessException;
import ru.otus.homework.library.exceptions.Errors;
import ru.otus.homework.library.mapper.GenreMapper;
import ru.otus.homework.library.repository.BookRepository;
import ru.otus.homework.library.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final GenreMapper genreMapper;

    @Override
    @Transactional
    public GenreResDTO add(GenreReqDTO genreReqDTO) {

        List<Genre> genres = genreRepository.findByNameEqualsIgnoreCase(genreReqDTO.getName());

        if(genres != null && genres.size() > 0) {
            throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, genreReqDTO.getName());
        }

        Genre genre = genreRepository.save(genreMapper.fromDto(genreReqDTO));
        return genreMapper.toDto(genre);
    }

    @Override
    @Transactional
    public GenreResDTO update(long id, GenreReqDTO genreReqDTO) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Genre genre = genreRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id));
        String name = genreReqDTO.getName();

        if(name != null && !name.trim().isEmpty()) {
            if (!genre.getName().equalsIgnoreCase(name)) {
                List<Genre> genres = genreRepository.findByNameEqualsIgnoreCase(name);
                if (genres != null && genres.size() > 0) {
                    throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, name);
                }
            }
        }

        Genre updateGenre = genreMapper.fromDto(genreReqDTO);
        updateGenre.setId(id);

        genreRepository.save(updateGenre);
        return genreMapper.toDto(updateGenre);

    }

    @Override
    @Transactional(readOnly = true)
    public GenreResDTO getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Genre genre = genreRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id));
        return genreMapper.toDto(genre);

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

        genreRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id));
        genreRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResListDTO> findByParams(String name) {
        List<Genre> genres;

        if(name != null && !name.isEmpty()) {
            genres = genreRepository.findByNameContainingIgnoreCase(name);
        }
        else {
            genres = genreRepository.findAll();
        }

        return genreMapper.toListDto(genres);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResListDTO> findAll() {
        List<Genre> genres = genreRepository.findAll();
        return genreMapper.toListDto(genres);
    }

}
