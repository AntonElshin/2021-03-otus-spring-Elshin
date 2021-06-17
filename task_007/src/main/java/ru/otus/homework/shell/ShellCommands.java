package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookCommentService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.GenreService;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BookCommentService bookCommentService;

    // Genres

    @ShellMethod(value = "Add genre", key = {"add_genre"})
    public void addGenre(
            @ShellOption(value = {"-name"}, defaultValue = "") String name,
            @ShellOption(value = {"-description"}, defaultValue = "") String description
    ) {
        genreService.add(name, description);
    }

    @ShellMethod(value = "Update genre", key = {"update_genre"})
    public void updateGenre(
            @ShellOption(value = {"-id"}) Long id,
            @ShellOption(value = {"-name"}, defaultValue = "") String name,
            @ShellOption(value = {"-description"}, defaultValue = "") String description
    ) {
        genreService.update(id, name, description);
    }

    @ShellMethod(value = "Get genre", key = {"get_genre"})
    public void getGenreById(@ShellOption(value = {"-id"}) Long id) {
        genreService.getById(id);
    }

    @ShellMethod(value = "Delete genre", key = {"delete_genre"})
    public void deleteGenreById(@ShellOption(value = {"-id"}) Long id) {
        genreService.deleteById(id);
    }

    @ShellMethod(value = "Find genres", key = {"find_genres"})
    public void findGenresByParams(@ShellOption(value = {"-name"}, defaultValue = "") String name) {
        genreService.findByParams(name);
    }

    @ShellMethod(value = "List genres", key = {"list_genres"})
    public void findAllGenres() {
        genreService.findAll();
    }

    @ShellMethod(value = "Count genres", key = {"count_genres"})
    public void countGenres() {
        genreService.count();
    }

    // Authors

    @ShellMethod(value = "Add author", key = {"add_author"})
    public void addAuthor(
            @ShellOption(value = {"-lastname"}, defaultValue = "") String lastname,
            @ShellOption(value = {"-firstname"}, defaultValue = "") String firstname,
            @ShellOption(value = {"-middlename"}, defaultValue = "") String middlename
    ) {
        authorService.add(lastname, firstname, middlename);
    }

    @ShellMethod(value = "Update author", key = {"update_author"})
    public void updateGenre(
            @ShellOption(value = {"-id"}) Long id,
            @ShellOption(value = {"-lastname"}, defaultValue = "") String lastname,
            @ShellOption(value = {"-firstname"}, defaultValue = "") String firstname,
            @ShellOption(value = {"-middlename"}, defaultValue = "") String middlename
    ) {
        authorService.update(id, lastname, firstname, middlename);
    }

    @ShellMethod(value = "Get author", key = {"get_author"})
    public void getAuthorById(@ShellOption(value = {"-id"}) Long id) {
        authorService.getById(id);
    }

    @ShellMethod(value = "Delete author", key = {"delete_author"})
    public void deleteAuthorById(@ShellOption(value = {"-id"}) Long id) {
        authorService.deleteById(id);
    }

    @ShellMethod(value = "Find authors", key = {"find_authors"})
    public void findAuthorsByParams(
            @ShellOption(value = {"-lastname"}, defaultValue = "") String lastname,
            @ShellOption(value = {"-firstname"}, defaultValue = "") String firstname,
            @ShellOption(value = {"-middlename"}, defaultValue = "") String middlename
    ) {
        authorService.findByParams(lastname, firstname, middlename);
    }

    @ShellMethod(value = "List authors", key = {"list_authors"})
    public void findAllAuthors() {
        authorService.findAll();
    }

    @ShellMethod(value = "Count authors", key = {"count_authors"})
    public void countAuthors() {
        authorService.count();
    }

    // Book

    @ShellMethod(value = "Add book", key = {"add_book"})
    public void addBook(
            @ShellOption(value = {"-title"}, defaultValue = "") String title,
            @ShellOption(value = {"-isbn"}, defaultValue = "") String isbn,
            @ShellOption(value = {"-description"}, defaultValue = "") String description,
            @ShellOption(value = {"-authors"}, defaultValue = "") String authors,
            @ShellOption(value = {"-genres"}, defaultValue = "") String genres
    ) {
        bookService.add(title, isbn, description, authors, genres);
    }

    @ShellMethod(value = "Update book", key = {"update_book"})
    public void updateBook(
            @ShellOption(value = {"-id"}) Long id,
            @ShellOption(value = {"-title"}, defaultValue = "") String title,
            @ShellOption(value = {"-isbn"}, defaultValue = "") String isbn,
            @ShellOption(value = {"-description"}, defaultValue = "") String description,
            @ShellOption(value = {"-authors"}, defaultValue = "") String authors,
            @ShellOption(value = {"-genres"}, defaultValue = "") String genres
    ) {
        bookService.update(id, title, isbn, description, authors, genres);
    }

    @ShellMethod(value = "Get book", key = {"get_book"})
    public void getBookById(@ShellOption(value = {"-id"}) Long id) {
        bookService.getById(id);
    }

    @ShellMethod(value = "Delete book", key = {"delete_book"})
    public void deleteBookById(@ShellOption(value = {"-id"}) Long id) {
        bookService.deleteById(id);
    }

    @ShellMethod(value = "Find books", key = {"find_books"})
    public void findBooksByParams(
            @ShellOption(value = {"-title"}, defaultValue = "") String title,
            @ShellOption(value = {"-isbn"}, defaultValue = "") String isbn,
            @ShellOption(value = {"-authorId"}, defaultValue = "") Long authorId,
            @ShellOption(value = {"-genreId"}, defaultValue = "") Long genreId
    ) {
        bookService.findByParams(title, isbn, authorId, genreId);
    }

    @ShellMethod(value = "List books", key = {"list_books"})
    public void findAllBooks() {
        bookService.findAll();
    }

    @ShellMethod(value = "Count books", key = {"count_books"})
    public void countBooks() {
        bookService.count();
    }

    // Book comment

    @ShellMethod(value = "Add book comment", key = {"add_book_comment"})
    public void addBookComment(
            @ShellOption(value = {"-bookId"}, defaultValue = "") Long bookId,
            @ShellOption(value = {"-text"}, defaultValue = "") String text
    ) {
        bookCommentService.add(bookId, text);
    }

    @ShellMethod(value = "Update book comment", key = {"update_book_comment"})
    public void updateBookComment(
            @ShellOption(value = {"-id"}) Long id,
            @ShellOption(value = {"-bookId"}, defaultValue = "") Long bookId,
            @ShellOption(value = {"-text"}, defaultValue = "") String text
    ) {
        bookCommentService.update(id, bookId, text);
    }

    @ShellMethod(value = "Get book comment", key = {"get_book_comment"})
    public void getBookCommentById(@ShellOption(value = {"-id"}) Long id) {
        bookCommentService.getById(id);
    }

    @ShellMethod(value = "Delete book comment", key = {"delete_book_comment"})
    public void deleteBookCommentById(@ShellOption(value = {"-id"}) Long id) {
        bookCommentService.deleteById(id);
    }

    @ShellMethod(value = "Find book comments", key = {"find_book_comments"})
    public void findBookCommentsByParams(
            @ShellOption(value = {"-bookId"}, defaultValue = "") Long bookId
    ) {
        bookCommentService.findAllByBookId(bookId);
    }

    @ShellMethod(value = "Count book comments", key = {"count_book_comments"})
    public void countBookComments(
            @ShellOption(value = {"-bookId"}, defaultValue = "") Long bookId
    ) {
        bookCommentService.countByBookId(bookId);
    }

}
