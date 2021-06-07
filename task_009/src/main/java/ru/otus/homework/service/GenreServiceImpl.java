package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.QBook;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.GenreDTO;
import ru.otus.homework.mapper.GenreMapper;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final ValidationService validationService;

    @Override
    @Transactional
    public GenreDTO add(GenreDTO genreDTO) {

        validationService.validateDTO(genreDTO);

        List<Genre> genres = genreRepository.findByNameEqualsIgnoreCase(genreDTO.getName());

        if(genres != null && genres.size() > 0) {
            throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, genreDTO.getName());
        }

        Genre genre = genreRepository.save(GenreMapper.INSTANCE.fromDto(genreDTO));
        return GenreMapper.INSTANCE.toDto(genre);
    }

    @Override
    @Transactional
    public GenreDTO update(long id, GenreDTO genreDTO) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        validationService.validateDTO(genreDTO);

        Optional<Genre> foundGenre = genreRepository.findById(id);

        if(!foundGenre.isPresent()) {
            throw new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id);
        }

        Genre genre = foundGenre.get();
        String name = genreDTO.getName();

        if(name != null && !name.trim().isEmpty()) {
            if (!genre.getName().equalsIgnoreCase(name)) {
                List<Genre> genres = genreRepository.findByNameEqualsIgnoreCase(name);
                if (genres != null && genres.size() > 0) {
                    throw new BusinessException(Errors.GENRE_WITH_NAME_EXISTS, name);
                }
            }
        }

        Genre updateGenre = GenreMapper.INSTANCE.fromDto(genreDTO);
        updateGenre.setId(id);

        genreRepository.save(updateGenre);
        return GenreMapper.INSTANCE.toDto(updateGenre);

    }

    @Override
    @Transactional(readOnly = true)
    public GenreDTO getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRE_ID);
        }

        Optional<Genre> foundGenre = genreRepository.findById(id);

        if(!foundGenre.isPresent()) {
            throw new BusinessException(Errors.GENRE_NOT_FOUND_BY_ID, id);
        }

        Genre genre = foundGenre.get();
        return GenreMapper.INSTANCE.toDto(genre);

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
    public List<GenreDTO> findByParams(String name) {
        List<Genre> genres;

        if(name != null && !name.isEmpty()) {
            genres = genreRepository.findByNameContainingIgnoreCase(name);
        }
        else {
            genres = genreRepository.findAll();
        }

        return genres.stream().map(GenreMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreDTO> findAll() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream().map(GenreMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

}
