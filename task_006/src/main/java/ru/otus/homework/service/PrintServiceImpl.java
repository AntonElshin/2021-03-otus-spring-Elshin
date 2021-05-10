package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class PrintServiceImpl implements PrintService {

    private PrintStream out;

    public String printGenre(Genre genre) {

        StringBuilder genreStr = new StringBuilder("");

        genreStr.append("Genre info:" + "\n");
        genreStr.append(prepareGenre(genre));

        print(genreStr.toString());

        return genreStr.toString();
    }

    public String printGenres(List<Genre> genres) {

        StringBuilder genreStr = new StringBuilder("");

        genreStr.append("List of genres:" + "\n");

        int index = 0;

        for(Genre genre : genres) {
            genreStr.append("----------------------------------------------------" + "\n");
            genreStr.append(prepareGenre(genre));
            if(index == genres.size() - 1) {
                genreStr.append("----------------------------------------------------" + "\n\n");
            }
            index++;
        }

        print(genreStr.toString());

        return genreStr.toString();

    }

    public String printGenresCount(int count) {

        String countStr = "\nGenres count: " + count + "\n\n";

        print(countStr);

        return countStr;

    }

    public String printAuthor(Author author) {

        StringBuilder authorStr = new StringBuilder("");

        authorStr.append("Author info:" + "\n");
        authorStr.append(prepareAuthor(author));

        print(authorStr.toString());

        return authorStr.toString();

    }

    public String printAuthors(List<Author> authors) {

        StringBuilder authorStr = new StringBuilder("");

        authorStr.append("List of authors:" + "\n");

        int index = 0;

        for(Author author : authors) {
            authorStr.append("----------------------------------------------------" + "\n");
            authorStr.append(prepareAuthor(author));
            if(index == authors.size() - 1) {
                authorStr.append("----------------------------------------------------" + "\n\n");
            }
            index++;
        }

        print(authorStr.toString());

        return authorStr.toString();

    }

    public String printAuthorsCount(int count) {

        String countStr = "\nAuthors count: " + count + "\n\n";

        print(countStr);

        return countStr;
    }

    @Override
    public String printBook(Book book) {

        StringBuilder bookStr = new StringBuilder("");

        bookStr.append("Book info:" + "\n");
        bookStr.append(prepareBook(book));

        print(bookStr.toString());

        return bookStr.toString();

    }

    @Override
    public String printBooks(List<Book> books) {

        StringBuilder bookStr = new StringBuilder("");

        bookStr.append("List of books:" + "\n");

        int index = 0;

        for(Book book : books) {
            bookStr.append("----------------------------------------------------" + "\n");
            bookStr.append(prepareBook(book));
            if(index == books.size() - 1) {
                bookStr.append("----------------------------------------------------" + "\n\n");
            }
            index++;
        }

        print(bookStr.toString());

        return bookStr.toString();

    }

    @Override
    public String printBooksCount(int count) {
        String countStr = "\nBooks count: " + count + "\n\n";

        print(countStr);

        return countStr;
    }

    private String prepareGenre(Genre genre) {

        StringBuilder genreStr = new StringBuilder("");

        genreStr.append("Id: " + genre.getId() + "\n");
        if(genre.getName() != null && !genre.getName().isBlank()) {
            genreStr.append("Name: " + genre.getName() + "\n");
        }
        if(genre.getDescription() != null && !genre.getDescription().isBlank()) {
            genreStr.append("Description: " + genre.getDescription() + "\n");
        }

        return genreStr.toString();

    }

    private String prepareAuthor(Author author) {

        StringBuilder authorStr = new StringBuilder("");

        String lastName = author.getLastName() != null && !author.getLastName().isBlank() ? author.getLastName() : "";
        String firstName = author.getFirstName() != null && !author.getFirstName().isBlank() ? author.getFirstName() : "";
        String middleName = author.getMiddleName() != null && !author.getMiddleName().isBlank() ? author.getMiddleName() : "";

        authorStr.append("Id: " + author.getId() + "\n");
        if(!lastName.isBlank()) {
            authorStr.append("Last name: " + lastName + "\n");
        }
        if(!firstName.isBlank()) {
            authorStr.append("First name: " + firstName + "\n");
        }
        if(!middleName.isBlank()) {
            authorStr.append("Middle name: " + middleName + "\n");
        }

        return authorStr.toString();

    }

    private String prepareBook(Book book) {

        StringBuilder bookStr = new StringBuilder("");

        bookStr.append("Id: " + book.getId() + "\n");
        if(book.getTitle() != null && !book.getTitle().isBlank()) {
            bookStr.append("Title: " + book.getTitle() + "\n");
        }
        if(book.getIsbn() != null && !book.getIsbn().isBlank()) {
            bookStr.append("ISBN: " + book.getIsbn() + "\n");
        }
        if(book.getDescription() != null && !book.getDescription().isBlank()) {
            bookStr.append("Description: " + book.getDescription() + "\n");
        }

        List<Author> authors = book.getAuthors();

        if(authors != null && authors.size() != 0) {
            bookStr.append("\nAuthors:");

            int index = 0;

            for(Author author : authors) {
                bookStr.append("\n");
                bookStr.append(prepareAuthor(author));
                if(index == authors.size() - 1) {
                    bookStr.append("\n");
                }
                index++;
            }
        }

        List<Genre> genres = book.getGenres();

        if(genres != null && genres.size() != 0) {
            bookStr.append("\nGenres:");

            int index = 0;

            for(Genre genre : genres) {
                bookStr.append("\n");
                bookStr.append(prepareGenre(genre));
                if(index == genres.size() - 1) {
                    bookStr.append("\n");
                }
                index++;
            }
        }

        return bookStr.toString();

    }

    private void print(String message) {

        configurateEnvironment();

        try {
            byte[] genreStrBytes = message.getBytes();
            out.write(genreStrBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configurateEnvironment() {
        out = System.out;
    }

}
