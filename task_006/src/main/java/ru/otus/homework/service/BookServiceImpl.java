package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookDao bookDao;

    private final PrintService printService;

    @Override
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
            List<Book> books = bookDao.getByParamsEqualsIgnoreCase(null, isbn, null, null);
            if(books != null && books.size() != 0) {
                throw new BusinessException(Errors.NOT_UNIQUE_BOOK_ISBN);
            }
        }

        Book book = new Book(title, isbn, description, new ArrayList<>(), new ArrayList<>());
        book = processLinks(book, authorStr, genreStr);

        List<Author> authors = book.getAuthors();
        List<Genre> genres = book.getGenres();

        if(authors == null || (authors != null && authors.size() == 0)) {
            throw new BusinessException(Errors.BOOK_SHOULD_HAVE_LEAST_ONE_AUTHOR);
        }
        if (genres == null || (genres != null && genres.size() == 0)) {
            throw new BusinessException(Errors.BOOK_SHOULD_HAVE_LEAST_ONE_GENRE);
        }

        bookDao.insert(0, book);
        return book;
    }

    @Override
    public void update(long id, String title, String isbn, String description, String authorStr, String genreStr) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookDao.getById(id);

        if(book == null) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, id);
        }

        Boolean changeFlag = false;

        if(title != null && !title.trim().isEmpty()) {
            book.setTitle(title);
            changeFlag = true;
        }
        if(isbn != null && !isbn.trim().isEmpty()) {

            if(!book.getIsbn().equalsIgnoreCase(isbn)) {
                List<Book> books = bookDao.getByParamsEqualsIgnoreCase(null, isbn, null, null);
                if(books != null && books.size() != 0) {
                    throw new BusinessException(Errors.NOT_UNIQUE_BOOK_ISBN);
                }
            }

            book.setIsbn(isbn);
            changeFlag = true;
        }
        if(description != null && !description.trim().isEmpty()) {
            book.setDescription(description);
            changeFlag = true;
        }

        if((authorStr != null && !authorStr.trim().isEmpty()) || (genreStr != null && !genreStr.trim().isEmpty())) {
            changeFlag = true;
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

        if(changeFlag) {
            bookDao.update(id, book);
        }

    }

    @Override
    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookDao.getById(id);

        if(book == null) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, id);
        }

        printService.printBook(book);

    }

    @Override
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_BOOK_ID);
        }

        Book book = bookDao.getById(id);

        if(book == null) {
            throw new BusinessException(Errors.BOOK_NOT_FOUND_BY_ID, id);
        }

        bookDao.deleteById(id);

    }

    @Override
    public void findByParams(String title, String isbn, Long authorId, Long genreId) {
        List<Book> books = bookDao.getByParamsLikeIgnoreCase(title, isbn, authorId, genreId);
        printService.printBooks(books);
    }

    @Override
    public void findAll() {
        List<Book> books = bookDao.getAll();
        printService.printBooks(books);
    }

    @Override
    public void count() {
        printService.printBooksCount(bookDao.count());
    }

    public List<String> getUniqueIds(String answer) {

        List<String> uniqueIds = new ArrayList<>();

        String[] answers = answer.trim().split(",");

        for(int j=0; j<answers.length; j++) {
            String givenId = answers[j];
            if(givenId.trim().isEmpty()) {
                continue;
            }

            Boolean foundFlag = false;

            for(String uniqueId : uniqueIds) {
                if(uniqueId.trim().equalsIgnoreCase(givenId)) {
                    foundFlag = true;
                    break;
                }
            }

            if(!foundFlag) {
                uniqueIds.add(givenId);
            }
        }

        return uniqueIds;

    }

    public Book processLinks(Book book, String authorStr, String genreStr) {

        if(authorStr != null && !authorStr.trim().isEmpty()) {
            List<String> authors = getUniqueIds(authorStr);

            List<Author> foundAuthors = authorDao.getByIds(authors);

            if(foundAuthors == null) {
                foundAuthors = new ArrayList<>();
            }

            if(authors.size() != foundAuthors.size()) {
                throw new BusinessException(Errors.AUTHORS_NOT_FOUND_BY_IDS);
            }

            book.setAuthors(foundAuthors);
        }

        if(genreStr != null && !genreStr.trim().isEmpty()) {
            List<String> genres = getUniqueIds(genreStr);

            List<Genre> foundGenres = genreDao.getByIds(genres);

            if(foundGenres == null) {
                foundGenres = new ArrayList<>();
            }

            if (genres.size() != foundGenres.size()) {
                throw new BusinessException(Errors.GENRES_NOT_FOUND_BY_IDS);
            }

            book.setGenres(foundGenres);
        }

        return book;

    }
}
