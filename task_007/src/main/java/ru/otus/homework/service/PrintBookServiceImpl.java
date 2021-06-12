package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.BookComment;
import ru.otus.homework.domain.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintBookServiceImpl implements PrintBookService {

    private final PrintService printService;
    private final PrintAuthorService printAuthorService;
    private final PrintGenreService printGenreService;
    private final PrintBookCommentService printBookCommentService;

    @Override
    public String printBook(Book book) {

        StringBuilder bookStr = new StringBuilder("");

        bookStr.append("Book info:" + "\n");
        bookStr.append(prepareBook(book, true));

        printService.print(bookStr.toString());

        return bookStr.toString();

    }

    @Override
    public String printBooks(List<Book> books) {

        StringBuilder bookStr = new StringBuilder("");

        bookStr.append("List of books:" + "\n");

        int index = 0;

        for(Book book : books) {
            bookStr.append("----------------------------------------------------" + "\n");
            bookStr.append(prepareBook(book, false));
            if(index == books.size() - 1) {
                bookStr.append("----------------------------------------------------" + "\n\n");
            }
            index++;
        }

        printService.print(bookStr.toString());

        return bookStr.toString();

    }

    @Override
    public String printBooksCount(Long count) {
        String countStr = "\nBooks count: " + count + "\n\n";

        printService.print(countStr);

        return countStr;
    }

    @Override
    public String prepareBook(Book book, Boolean commentsFlag) {

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
                bookStr.append(printAuthorService.prepareAuthor(author));
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
                bookStr.append(printGenreService.prepareGenre(genre));
                if(index == genres.size() - 1) {
                    bookStr.append("\n");
                }
                index++;
            }
        }

        if(commentsFlag) {

            List<BookComment> bookComments = book.getComments();

            if(bookComments != null && bookComments.size() != 0) {
                bookStr.append("\nBook comments:");

                int index = 0;

                for(BookComment bookComment : bookComments) {
                    bookStr.append("\n");
                    bookStr.append(printBookCommentService.prepareBookComment(bookComment));
                    if(index == bookComments.size() - 1) {
                        bookStr.append("\n");
                    }
                    index++;
                }
            }

        }


        return bookStr.toString();

    }
}
