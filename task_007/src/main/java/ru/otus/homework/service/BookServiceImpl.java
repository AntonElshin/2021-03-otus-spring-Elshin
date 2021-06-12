package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.repository.AuthorRepositoryJpa;
import ru.otus.homework.repository.BookRepositoryJpa;
import ru.otus.homework.repository.GenreRepositoryJpa;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.utils.Utils;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepositoryJpa authorRepositoryJpa;
    private final GenreRepositoryJpa genreRepositoryJpa;
    private final BookRepositoryJpa bookRepositoryJpa;

    private final PrintBookService printBookService;

    @Override
    @Transactional
    public Book add(String title, String isbn, String description, String authorStr, String genreStr) {

        if(title == null || (title != null && title.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_TITLE);
        }
        if(authorStr == null || (authorStr != null && authorStr.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHORS);
        }
        if(genreStr == null || (genreStr != null && genreStr.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_GENRES);
        }

        if(isbn != null && !isbn.trim().isEmpty()) {
            List<Book> books = bookRepositoryJpa.findByParamsEqualsIgnoreCase(null, isbn, null, null);
            if(books != null && books.size() != 0) {
                throw new BusinessException(Errors.NOT_UNIQUE_BOOK_ISBN);
            }
        }

        Book book = new Book(title, isbn, description, new ArrayList<>(), new ArrayList<>(), null);
        book = processLinks(book, authorStr, genreStr);

        List<Author> authors = book.getAuthors();
        List<Genre> genres = book.getGenres();

        if(authors == null || (authors != null && authors.size() == 0)) {
            throw new BusinessException(Errors.BOOK_SHOULD_HAVE_LEAST_ONE_AUTHOR);
        }
        if (genres == null || (genres != null && genres.size() == 0)) {
            throw new BusinessException(Errors.BOOK_SHOULD_HAVE_LEAST_ONE_GENRE);
        }

        bookRepositoryJpa.save(book);
        return book;
    }

    @Override
    @Transactional
    public void update(long id, String title, String isbn, String description, String authorStr, String genreStr) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, id));

        if(title != null && !title.trim().isEmpty()) {
            book.setTitle(title);
        }
        if(isbn != null && !isbn.trim().isEmpty()) {

            if(!book.getIsbn().equalsIgnoreCase(isbn)) {
                List<Book> books = bookRepositoryJpa.findByParamsEqualsIgnoreCase(null, isbn, null, null);
                if(books != null && books.size() != 0) {
                    throw new BusinessException(Errors.NOT_UNIQUE_BOOK_ISBN);
                }
            }

            book.setIsbn(isbn);
        }
        if(description != null && !description.trim().isEmpty()) {
            book.setDescription(description);
        }

        if((authorStr != null && !authorStr.trim().isEmpty()) || (genreStr != null && !genreStr.trim().isEmpty())) {
            book = processLinks(book, authorStr, genreStr);

            if(authorStr != null && !authorStr.trim().isEmpty()) {

                List<Author> authors = book.getAuthors();

                if(authors == null || (authors != null && authors.size() == 0)) {
                    throw new BusinessException(Errors.BOOK_SHOULD_HAVE_LEAST_ONE_AUTHOR);
                }

            }

            if(genreStr != null && !genreStr.trim().isEmpty()) {

                List<Genre> genres = book.getGenres();

                if (genres == null || (genres != null && genres.size() == 0)) {
                    throw new BusinessException(Errors.BOOK_SHOULD_HAVE_LEAST_ONE_GENRE);
                }
            }


        }

        bookRepositoryJpa.save(book);

    }

    @Override
    @Transactional(readOnly = true)
    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, id));
        printBookService.printBook(book);

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, id));
        bookRepositoryJpa.delete(book);

    }

    @Override
    @Transactional(readOnly = true)
    public void findByParams(String title, String isbn, Long authorId, Long genreId) {
        List<Book> books = bookRepositoryJpa.findByParamsLikeIgnoreCase(title, isbn, authorId, genreId);
        printBookService.printBooks(books);
    }

    @Override
    @Transactional(readOnly = true)
    public void findAll() {
        List<Book> books = bookRepositoryJpa.findAll();
        printBookService.printBooks(books);
    }

    @Override
    @Transactional(readOnly = true)
    public void count() {
        printBookService.printBooksCount(bookRepositoryJpa.count());
    }

    @Transactional(readOnly = true)
    public Book processLinks(Book book, String authorStr, String genreStr) {

        if(authorStr != null && !authorStr.trim().isEmpty()) {
            List<Long> authorIds = Utils.getUniqueIds(authorStr);

            List<Author> foundAuthors = authorRepositoryJpa.findByIds(authorIds);

            if(foundAuthors == null) {
                foundAuthors = new ArrayList<>();
            }

            if(authorIds.size() != foundAuthors.size()) {
                throw new BusinessException(Errors.AUTHORS_NOT_FOUND_BY_IDS);
            }

            book.setAuthors(foundAuthors);
        }

        if(genreStr != null && !genreStr.trim().isEmpty()) {
            List<Long> genreIds = Utils.getUniqueIds(genreStr);

            List<Genre> foundGenres = genreRepositoryJpa.findByIds(genreIds);

            if(foundGenres == null) {
                foundGenres = new ArrayList<>();
            }

            if (genreIds.size() != foundGenres.size()) {
                throw new BusinessException(Errors.GENRES_NOT_FOUND_BY_IDS);
            }

            book.setGenres(foundGenres);
        }

        return book;

    }

}
