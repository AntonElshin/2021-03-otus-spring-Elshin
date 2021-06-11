package ru.otus.homework.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.QAuthor;
import ru.otus.homework.domain.QBook;
import ru.otus.homework.dto.AuthorReqDTO;
import ru.otus.homework.dto.AuthorResDTO;
import ru.otus.homework.dto.AuthorResListDTO;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;
import ru.otus.homework.mapper.AuthorMapper;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorResDTO add(AuthorReqDTO authorReqDTO) {
        Author author = authorRepository.save(authorMapper.fromDto(authorReqDTO));
        return authorMapper.toDto(author);
    }

    @Override
    @Transactional
    public AuthorResDTO update(long id, AuthorReqDTO authorReqDTO) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        authorRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id));

        Author author = authorMapper.fromDto(authorReqDTO);
        author.setId(id);

        authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResDTO getById(long id) {

        if(id == 0) {
            throw new BusinessException(Errors.MISSING_REQUIRED_PARAM_AUTHOR_ID);
        }

        Author author = authorRepository.findById(id).orElseThrow(() -> new BusinessException(Errors.AUTHOR_NOT_FOUND_BY_ID, id));
        return authorMapper.toDto(author);

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
    public List<AuthorResListDTO> findByParams(String lastName, String firstName, String middleName) {

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

        return authorMapper.toListDto(authors);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResListDTO> findAll() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toListDto(authors);
    }

}
