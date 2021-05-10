package ru.otus.homework.dao;

import liquibase.util.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.dao.ext.BookAuthor;
import ru.otus.homework.dao.ext.BookGenre;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;

import ru.otus.homework.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(*) from lib_books", Integer.class);
    }

    @Override
    public void insert(long id, Book book) {

        long bookId = 0;

        if(id == 0) {
            bookId  = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select nextval('lib_books_seq')", Long.class);
        }
        else {
            bookId = id;
        }

        namedParameterJdbcOperations.update("insert into lib_books (bookid, title, isbn, description) values (:id, :title, :isbn, :description)",
                Map.of("id", bookId,
                        "title", book.getTitle(),
                        "isbn", book.getIsbn(),
                        "description", book.getDescription()));

        createBookLinks(bookId, book);

    }

    @Override
    public void update(long id, Book book) {

        namedParameterJdbcOperations.update("update lib_books set title = :title, isbn = :isbn, description = :description where bookid = :id",
                Map.of("id", id, "title", book.getTitle(),"isbn", book.getIsbn(),"description", book.getDescription()));

        deleteBookLinks(id);
        createBookLinks(id, book);

    }

    @Override
    public Book getById(long id) {

        Map<String, Object> params = Collections.singletonMap("id", id);
        Book book = namedParameterJdbcOperations.queryForObject(
                "select bookid, title, isbn, description from lib_books where bookid = :id",
                params,
                new BookMapper()
        );

        List<Book> books = getBooksLinks(List.of(book));

        if(books != null && books.size() != 0) {
            book.setAuthors(books.get(0).getAuthors());
            book.setGenres(books.get(0).getGenres());
        }

        return book;

    }

    @Override
    public void deleteById(long id) {

        deleteBookLinks(id);

        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from lib_books where bookid = :id", params
        );

    }

    @Override
    public List<Book> getAll() {

        String query = "select bookid, title, isbn, description from lib_books";

        List<Book> books = namedParameterJdbcOperations.getJdbcOperations().query(query, new BookMapper());

        if(books != null && books.size() != 0) {
            books = getBooksLinks(books);
        }

        return books;

    }

    @Override
    public List<Book> getByParamsEqualsIgnoreCase(String title, String isbn, Long authorId, Long genreId) {

        if(title != null && !title.isEmpty()) {
            title = title.toLowerCase();
        }

        String query = "select b.bookid, b.title, b.isbn, b.description from lib_books b";

        if(authorId != null) {
            query += " inner join lib_bookauthorlinks ba on ba.bookid = b.bookid and ba.authorid = " + authorId;
        }
        if(genreId != null) {
            query += " inner join lib_bookgenrelinks bg on bg.bookid = b.bookid and bg.genreid = " + genreId;
        }

        query += " where 1=1";

        if(title != null && !title.isEmpty()) {
            query += " and lower(title) = '" + title.toLowerCase() + "'";
        }
        if(isbn != null && !isbn.isEmpty()) {
            query += " and lower(isbn) = '" + isbn.toLowerCase() + "'";
        }

        List<Book> books = namedParameterJdbcOperations.getJdbcOperations().query(query, new BookMapper());

        if(books != null && books.size() != 0) {
            books = getBooksLinks(books);
        }

        return books;
    }

    @Override
    public List<Book> getByParamsLikeIgnoreCase(String title, String isbn, Long authorId, Long genreId) {

        if(title != null && !title.isEmpty()) {
            title = title.toLowerCase();
        }

        String query = "select b.bookid, b.title, b.isbn, b.description from lib_books b";

        if(authorId != null) {
            query += " inner join lib_bookauthorlinks ba on ba.bookid = b.bookid and ba.authorid = " + authorId;
        }
        if(genreId != null) {
            query += " inner join lib_bookgenrelinks bg on bg.bookid = b.bookid and bg.genreid = " + genreId;
        }

        query += " where 1=1";

        if(title != null && !title.isEmpty()) {
            query += " and lower(title) like '%" + title.toLowerCase()  + "%'";
        }
        if(isbn != null && !isbn.isEmpty()) {
            query += " and lower(isbn) like '%" + isbn.toLowerCase()  + "%'";
        }

        List<Book> books = namedParameterJdbcOperations.getJdbcOperations().query(query, new BookMapper());

        if(books != null && books.size() != 0) {
            books = getBooksLinks(books);
        }

        return books;
    }

    public List<Book> getBooksLinks(List<Book> books) {

        List<String> bookIds = new ArrayList<>();

        for(Book book : books) {
            bookIds.add("" + book.getId());
        }

        List<BookAuthor> bookAuthors = getBookAuthors(bookIds);
        List<BookGenre> bookGenres = getBookGenres(bookIds);

        for(Book book : books) {

            for(BookAuthor bookAuthor : bookAuthors) {
                if(bookAuthor.getBookId() == book.getId()) {
                    book.getAuthors().add(bookAuthor.getAuthor());
                }
            }

            for(BookGenre bookGenre : bookGenres) {
                if(bookGenre.getBookId() == book.getId()) {
                    book.getGenres().add(bookGenre.getGenre());
                }
            }

        }

        return books;

    }

    public List<BookAuthor> getBookAuthors(List<String> bookIds) {
        return namedParameterJdbcOperations.getJdbcOperations().query("select ba.bookid, a.authorid, a.lastname, a.firstname, a.middlename from lib_authors a inner join lib_bookauthorlinks ba on ba.authorid = a.authorid where ba.bookid in (" + StringUtils.join(bookIds, ", ") + ")", new BookAuthorMapper());
    }

    public List<BookGenre> getBookGenres(List<String> bookIds) {
        return namedParameterJdbcOperations.getJdbcOperations().query("select bg.bookid, g.genreid, g.name, g.description from lib_genres g inner join lib_bookgenrelinks bg on bg.genreid = g.genreid where bg.bookid in (" + StringUtils.join(bookIds, ", ") + ")", new BookGenreMapper());
    }

    private void createBookLinks(long bookId, Book book) {

        if(book.getAuthors() != null && book.getAuthors().size() != 0) {
            String queryBookAuthorLink = "insert into lib_bookauthorlinks (bookid, authorid) values ";
            int count = 0;
            for(Author author : book.getAuthors()) {
                queryBookAuthorLink += "(" + bookId + ", " + author.getId() + ")";
                if(count != book.getAuthors().size() - 1) {
                    queryBookAuthorLink += ",";
                }
                count++;
            }

            namedParameterJdbcOperations.update(queryBookAuthorLink, Map.of("id", 1));
        }

        if(book.getGenres() != null && book.getGenres().size() != 0) {
            String queryBookGenreLink = "insert into lib_bookgenrelinks (bookid, genreid) values ";
            int count = 0;
            for(Genre genre : book.getGenres()) {
                queryBookGenreLink += "(" + bookId + ", " + genre.getId() + ")";
                if(count != book.getGenres().size() - 1) {
                    queryBookGenreLink += ",";
                }
                count++;
            }

            namedParameterJdbcOperations.update(queryBookGenreLink, Map.of("id", 1));
        }

    }

    private void deleteBookLinks(long bookId) {

        Map<String, Object> params = Collections.singletonMap("id", bookId);
        namedParameterJdbcOperations.update(
                "delete from lib_bookauthorlinks where bookid = :id", params
        );
        namedParameterJdbcOperations.update(
                "delete from lib_bookgenrelinks where bookid = :id", params
        );

    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("bookid");
            String title = resultSet.getString("title");
            String isbn = resultSet.getString("isbn");
            String description = resultSet.getString("description");
            return new Book(id, title, isbn, description, new ArrayList<>(), new ArrayList<>());
        }
    }

    private static class BookGenreMapper implements RowMapper<ru.otus.homework.dao.ext.BookGenre> {

        @Override
        public BookGenre mapRow(ResultSet resultSet, int i) throws SQLException {
            long bookid = resultSet.getLong("bookid");
            long genreid = resultSet.getLong("genreid");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            return new BookGenre(bookid, new Genre(genreid, name, description));
        }
    }

    private static class BookAuthorMapper implements RowMapper<BookAuthor> {

        @Override
        public BookAuthor mapRow(ResultSet resultSet, int i) throws SQLException {
            long bookid = resultSet.getLong("bookid");
            long authorid = resultSet.getLong("authorid");
            String lastname = resultSet.getString("lastname");
            String firstname = resultSet.getString("firstname");
            String middlename = resultSet.getString("middlename");
            return new BookAuthor(bookid, new Author(authorid, lastname, firstname, middlename));
        }
    }

}

