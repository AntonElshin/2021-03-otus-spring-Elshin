package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintAuthorServiceImpl implements PrintAuthorService {

    private final PrintService printService;

    @Override
    public String printAuthor(Author author) {

        StringBuilder authorStr = new StringBuilder("");

        authorStr.append("Author info:" + "\n");
        authorStr.append(prepareAuthor(author));

        printService.print(authorStr.toString());

        return authorStr.toString();

    }

    @Override
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

        printService.print(authorStr.toString());

        return authorStr.toString();

    }

    @Override
    public String printAuthorsCount(Long count) {

        String countStr = "\nAuthors count: " + count + "\n\n";

        printService.print(countStr);

        return countStr;
    }

    @Override
    public String prepareAuthor(Author author) {

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

}
