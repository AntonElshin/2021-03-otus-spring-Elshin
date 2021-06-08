package ru.otus.homework.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpaImpl implements BookRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(b) from Book b", Long.class);
        return query.getSingleResult();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public void delete(Book book) {
        em.remove(book);
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b order by b.id", Book.class);
        return query.getResultList();
    }

    @Override
    public List<Book> findByParamsEqualsIgnoreCase(String title, String isbn, Long authorId, Long genreId) {

        String queryStr = "select b from Book b";

        if(authorId != null) {
            queryStr += " inner join b.authors a on a.id = :authorId";
        }
        if(genreId != null) {
            queryStr += " inner join b.genres g on g.id = :genreId";
        }

        queryStr += " where 1=1";

        if(title != null && !title.isEmpty()) {
            queryStr += " and lower(b.title) = :title";
        }
        if(isbn != null && !isbn.isEmpty()) {
            queryStr += " and lower(b.isbn) = :isbn";
        }

        queryStr += " order by b.id";

        TypedQuery<Book> query = prepareConditions(queryStr, title, isbn, authorId, genreId);
        return query.getResultList();
    }

    @Override
    public List<Book> findByParamsLikeIgnoreCase(String title, String isbn, Long authorId, Long genreId) {

        String queryStr = "select b from Book b";

        if(authorId != null) {
            queryStr += " inner join b.authors a on a.id = :authorId";
        }
        if(genreId != null) {
            queryStr += " inner join b.genres g on g.id = :genreId";
        }

        queryStr += " where 1=1";

        if(title != null && !title.isEmpty()) {
            title = "%" + title + "%";
            queryStr += " and lower(b.title) like :title";
        }
        if(isbn != null && !isbn.isEmpty()) {
            isbn = "%" + isbn + "%";
            queryStr += " and lower(b.isbn) like :isbn";
        }

        queryStr += " order by b.id";

        TypedQuery<Book> query = prepareConditions(queryStr, title, isbn, authorId, genreId);
        return query.getResultList();

    }

    private TypedQuery<Book> prepareConditions(String queryStr, String title, String isbn, Long authorId, Long genreId) {

        TypedQuery<Book> query = em.createQuery(queryStr,
                Book.class);
        if(title != null && !title.isEmpty()) {
            query.setParameter("title", title.toLowerCase());
        }
        if(isbn != null && !isbn.isEmpty()) {
            query.setParameter("isbn", isbn.toLowerCase());
        }
        if(authorId != null) {
            query.setParameter("authorId", authorId);
        }
        if(genreId != null) {
            query.setParameter("genreId", genreId);
        }

        return query;

    }
}
