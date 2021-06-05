package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.AuthorRepositoryJpa;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.repository.BookRepositoryJpa;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepositoryJpa authorRepositoryJpa;
    private final BookRepositoryJpa bookRepositoryJpa;
    private final PrintService printService;

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

        authorRepositoryJpa.save(author);
        return author;
    }

    @Override
    @Transactional
    public void update(long id, String lastName, String firstName, String middleName) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        Optional<Author> foundAuthor = authorRepositoryJpa.findById(id);

        if(!foundAuthor.isPresent()) {
            throw new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id);
        }

        Author author = foundAuthor.get();

        if(lastName != null && !lastName.trim().isEmpty()) {
            author.setLastName(lastName);
        }
        if(firstName != null && !firstName.trim().isEmpty()) {
            author.setFirstName(firstName);
        }
        if(middleName != null && !middleName.trim().isEmpty()) {
            author.setMiddleName(middleName);
        }

        authorRepositoryJpa.save(author);

    }

    @Override
    @Transactional(readOnly = true)
    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        Optional<Author> foundAuthor = authorRepositoryJpa.findById(id);

        if(!foundAuthor.isPresent()) {
            throw new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id);
        }

        printService.printAuthor(foundAuthor.get());

    }

    @Override
    @Transactional
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        List<Book> books = bookRepositoryJpa.findByParamsEqualsIgnoreCase(null, null, id, null);

        if(books != null && books.size() > 0) {
            throw new BusinessException(Errors.AUTHOR_HAS_LINKED_BOOKS);
        }

        authorRepositoryJpa.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public void findByParams(String lastName, String firstName, String middleName) {
        List<Author> authors = authorRepositoryJpa.findByParamsLikeIgnoreCase(lastName, firstName, middleName);
        printService.printAuthors(authors);
    }

    @Override
    @Transactional(readOnly = true)
    public void findAll() {
        List<Author> authors = authorRepositoryJpa.findAll();
        printService.printAuthors(authors);
    }

    @Override
    @Transactional(readOnly = true)
    public void count() {
        printService.printAuthorsCount(authorRepositoryJpa.count());
    }

}
