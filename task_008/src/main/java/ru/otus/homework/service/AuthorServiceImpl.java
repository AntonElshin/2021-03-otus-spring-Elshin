package ru.otus.homework.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.QBook;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.domain.QAuthor;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PrintAuthorService printAuthorService;

    @Override
    @Transactional
    public Author add(String lastName, String firstName, String middleName) {

        if(lastName == null || (lastName != null && lastName.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_LAST_NAME);
        }
        if(firstName == null || (firstName != null && firstName.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_FIRST_NAME);
        }

        Author author = new Author(lastName, firstName, middleName);

        authorRepository.save(author);
        return author;
    }

    @Override
    @Transactional
    public void update(long id, String lastName, String firstName, String middleName) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        Author author = authorRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id));

        if(lastName != null && !lastName.trim().isEmpty()) {
            author.setLastName(lastName);
        }
        if(firstName != null && !firstName.trim().isEmpty()) {
            author.setFirstName(firstName);
        }
        if(middleName != null && !middleName.trim().isEmpty()) {
            author.setMiddleName(middleName);
        }

        authorRepository.save(author);

    }

    @Override
    @Transactional(readOnly = true)
    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        Author author = authorRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id));
        printAuthorService.printAuthor(author);

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        List<Book> books = bookRepository.findAll(QBook.book.authors.any().id.eq(id));

        if(books != null && books.size() > 0) {
            throw new BusinessException(Errors.AUTHOR_HAS_LINKED_BOOKS);
        }

        authorRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id));
        authorRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public void findByParams(String lastName, String firstName, String middleName) {

        List<BooleanExpression> predicates = new ArrayList<>();

        if(lastName != null && !lastName.isEmpty()) {
            predicates.add(QAuthor.author.lastName.containsIgnoreCase(lastName));
        }
        if(firstName != null && !firstName.isEmpty()) {
            predicates.add(QAuthor.author.firstName.containsIgnoreCase(firstName));
        }
        if(middleName != null && !middleName.isEmpty()) {
            predicates.add(QAuthor.author.middleName.containsIgnoreCase(middleName));
        }

        List<Author> authors;

        if(predicates.size() == 0) {
            authors = authorRepository.findAll();
        }
        else if(predicates.size() == 1) {
            authors = authorRepository.findAll(predicates.get(0));
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
            authors = authorRepository.findAll(fullPredicate);
        }

        printAuthorService.printAuthors(authors);
    }

    @Override
    @Transactional(readOnly = true)
    public void findAll() {
        List<Author> authors = authorRepository.findAll();
        printAuthorService.printAuthors(authors);
    }

    @Override
    @Transactional(readOnly = true)
    public void count() {
        printAuthorService.printAuthorsCount(authorRepository.count());
    }

}
