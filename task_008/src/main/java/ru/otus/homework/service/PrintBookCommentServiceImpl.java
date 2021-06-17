package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.BookComment;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintBookCommentServiceImpl implements PrintBookCommentService {

    private final PrintService printService;

    @Override
    public String printBookComment(BookComment bookComment) {

        StringBuilder bookCommentStr = new StringBuilder("");

        bookCommentStr.append("Book comment info:" + "\n");
        bookCommentStr.append(prepareBookComment(bookComment));

        printService.print(bookCommentStr.toString());

        return bookCommentStr.toString();
    }

    @Override
    public String printBookComments(List<BookComment> bookComments) {

        StringBuilder bookCommentStr = new StringBuilder("");

        bookCommentStr.append("List of book comments:" + "\n");

        int index = 0;

        for(BookComment bookComment : bookComments) {
            bookCommentStr.append("----------------------------------------------------" + "\n");
            bookCommentStr.append(prepareBookComment(bookComment));
            if(index == bookComments.size() - 1) {
                bookCommentStr.append("----------------------------------------------------" + "\n\n");
            }
            index++;
        }

        printService.print(bookCommentStr.toString());

        return bookCommentStr.toString();

    }

    @Override
    public String printBookCommentsCount(Long count) {
        String countStr = "\nBook comments count: " + count + "\n\n";

        printService.print(countStr);

        return countStr;
    }

    @Override
    public String prepareBookComment(BookComment bookComment) {

        StringBuilder bookCommentStr = new StringBuilder("");

        bookCommentStr.append("Id: " + bookComment.getId() + "\n");
        if(bookComment.getText() != null && !bookComment.getText().isBlank()) {
            bookCommentStr.append("Text: " + bookComment.getText() + "\n");
        }

        return bookCommentStr.toString();
    }

}
