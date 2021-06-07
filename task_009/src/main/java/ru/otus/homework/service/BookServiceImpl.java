package ru.otus.homework.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.AuthorDTO;
import ru.otus.homework.dto.BookDTO;
import ru.otus.homework.dto.GenreDTO;
import ru.otus.homework.mapper.AuthorMapper;
import ru.otus.homework.mapper.BookMapper;
import ru.otus.homework.mapper.GenreMapper;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.QBook;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final ValidationService validationService;

    @Override
    @Transactional
    public BookDTO add(BookDTO bookDTO) {

        validationService.validateDTO(bookDTO);
        String isbn = bookDTO.getIsbn();

        if(isbn != null && !isbn.trim().isEmpty()) {
            List<Book> books = bookRepository.findAll(QBook.book.isbn.equalsIgnoreCase(isbn));
            if(books != null && books.size() != 0) {
                throw new BusinessException(Errors.NOT_UNIQUE_BOOK_ISBN);
            }
        }

        Book book = processLinks(bookDTO);

        bookRepository.save(book);
        return BookMapper.INSTANCE.toDto(book);
    }

    @Override
    @Transactional
    public BookDTO update(long id, BookDTO bookDTO) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        validationService.validateDTO(bookDTO);

        Optional<Book> foundBook = bookRepository.findById(id);

        if(!foundBook.isPresent()) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, id);
        }

        Book book = foundBook.get();
        String isbn = bookDTO.getIsbn();

        if(isbn != null && !isbn.trim().isEmpty()) {
            if(!book.getIsbn().equalsIgnoreCase(isbn)) {
                List<Book> books = bookRepository.findAll(QBook.book.isbn.equalsIgnoreCase(isbn));
                if(books != null && books.size() != 0) {
                    throw new BusinessException(Errors.NOT_UNIQUE_BOOK_ISBN);
                }
            }
        }

        Book updateBook = processLinks(bookDTO);
        updateBook.setId(id);

        bookRepository.save(updateBook);
        return BookMapper.INSTANCE.toDto(updateBook);

    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Optional<Book> foundBook = bookRepository.findById(id);

        if(!foundBook.isPresent()) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, id);
        }

        Book book = foundBook.get();
        return BookMapper.INSTANCE.toDto(book);

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        bookRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findByParams(String title, String isbn, Long authorId, Long genreId) {

        List<BooleanExpression> predicates = new ArrayList<>();

        if(title != null && !title.isEmpty()) {
            predicates.add(QBook.book.title.containsIgnoreCase(title));
        }
        if(isbn != null && !isbn.isEmpty()) {
            predicates.add(QBook.book.isbn.containsIgnoreCase(isbn));
        }
        if(authorId != null) {
            predicates.add(QBook.book.authors.any().id.eq(authorId));
        }
        if(genreId != null) {
            predicates.add(QBook.book.genres.any().id.eq(genreId));
        }

        List<Book> books;

        if(predicates.size() == 0) {
            books = bookRepository.findAll();
        }
        else if(predicates.size() == 1) {
            books = bookRepository.findAll(predicates.get(0));
        }
        else {
            BooleanExpression fullPredicate = predicates.get(0);
            int index = 0;
            for(BooleanExpression predicate : predicates) {
                if(index < predicates.size() - 1) {
                    index++;
                    fullPredicate = fullPredicate.and(predicates.get(index));
                }
            }
            books = bookRepository.findAll(fullPredicate);
        }

        return books.stream().map(BookMapper.INSTANCE::toDto).collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(BookMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public Book processLinks(BookDTO bookDTO) {

        List<AuthorDTO> authors = bookDTO.getAuthors();
        List<GenreDTO> genres = bookDTO.getGenres();

        List<Long> authorIds = new ArrayList<>();
        List<Long> genreIds = new ArrayList<>();

        if(authors != null) {

            for(AuthorDTO authorDTO : authors) {
                Boolean foundFlag = false;

                for(Long id : authorIds) {
                    if(id.equals(authorDTO.getId())) {
                        foundFlag = true;
                        break;
                    }
                }

                if(!foundFlag) {
                    authorIds.add(authorDTO.getId());
                }
            }
        }

        if(genres != null) {

            for(GenreDTO genreDTO : genres) {
                Boolean foundFlag = false;

                for(Long id : genreIds) {
                    if(id.equals(genreDTO.getId())) {
                        foundFlag = true;
                        break;
                    }
                }

                if(!foundFlag) {
                    genreIds.add(genreDTO.getId());
                }
            }
        }

        List<Author> foundAuthors = authorRepository.findByIds(authorIds);
        List<Genre> foundGenres = genreRepository.findByIds(genreIds);

        if(foundAuthors == null) {
            foundAuthors = new ArrayList<>();
        }

        if(authorIds.size() != foundAuthors.size()) {
            throw new BusinessException(Errors.AUTHORS_NOT_FOUND_BY_IDS);
        }

        if(foundGenres == null) {
            foundGenres = new ArrayList<>();
        }

        if (genreIds.size() != foundGenres.size()) {
            throw new BusinessException(Errors.GENRES_NOT_FOUND_BY_IDS);
        }

        if(foundAuthors == null || (foundAuthors != null && foundAuthors.size() == 0)) {
            throw new BusinessException(Errors.BOOK_SHOULD_HAVE_LEAST_ONE_AUTHOR);
        }
        if (foundGenres == null || (foundGenres != null && foundGenres.size() == 0)) {
            throw new BusinessException(Errors.BOOK_SHOULD_HAVE_LEAST_ONE_GENRE);
        }

        bookDTO.setAuthors(new ArrayList<>());
        bookDTO.setGenres(new ArrayList<>());

        Book book = BookMapper.INSTANCE.fromDto(bookDTO);
        book.setAuthors(foundAuthors);
        book.setGenres(foundGenres);

        return book;

    }

}
