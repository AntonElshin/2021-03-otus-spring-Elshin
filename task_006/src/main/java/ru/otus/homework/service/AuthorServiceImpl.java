package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;
    private final PrintService printService;

    @Override
    public Author add(String lastName, String firstName, String middleName) {

        if(lastName == null || (lastName != null && lastName.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_LAST_NAME);
        }
        if(firstName == null || (firstName != null && firstName.trim().isEmpty())) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_FIRST_NAME);
        }

        Author author = new Author(lastName, firstName, middleName);

        authorDao.insert(0, author);
        return author;
    }

    @Override
    public void update(long id, String lastName, String firstName, String middleName) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        Author author = getAuthorById(id);

        if(author == null) {
            throw new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id);
        }

        Boolean changeFlag = false;

        if(firstName != null && !firstName.trim().isEmpty()) {
            author.setFirstName(firstName);
            changeFlag = true;
        }
        if(lastName != null && !lastName.trim().isEmpty()) {
            author.setLastName(lastName);
            changeFlag = true;
        }
        if(middleName != null && !middleName.trim().isEmpty()) {
            author.setMiddleName(middleName);
            changeFlag = true;
        }

        if(changeFlag) {
            authorDao.update(id, author);
        }

    }

    @Override
    public void getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        Author author = getAuthorById(id);

        if(author == null) {
            throw new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id);
        }

        printService.printAuthor(author);

    }

    @Override
    public void deleteById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        Author author = getAuthorById(id);

        if(author == null) {
            throw new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id);
        }

        if(authorDao.getLinkedBookCount(id) != 0) {
            throw new BusinessException(Errors.AUTHOR_HAS_LINKED_BOOKS);
        }

        authorDao.deleteById(id);

    }

    @Override
    public void findByParams(String lastName, String firstName, String middleName) {
        List<Author> authors = authorDao.getByParamsLikeIgnoreCase(lastName, firstName, middleName);
        printService.printAuthors(authors);
    }

    @Override
    public void findAll() {
        List<Author> authors = authorDao.getAll();
        printService.printAuthors(authors);
    }

    @Override
    public void count() {
        printService.printAuthorsCount(authorDao.count());
    }

    private Author getAuthorById(long id) {

        Author author = null;

        try {
            author = authorDao.getById(id);
        }
        catch (Exception exception) {
            System.out.println("Автор не найден по идентификатору: " + id);
        }

        return author;

    }
}
