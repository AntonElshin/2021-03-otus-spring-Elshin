package ru.otus.homework.repository;

import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class BookCommentRepositoryJpaImpl implements BookCommentRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long countByBookId(long bookId) {
        TypedQuery<Long> query = em.createQuery("select count(bc) from BookComment bc where bc.book.id = :bookId", Long.class);
        query.setParameter("bookId", bookId);
        return query.getSingleResult();
    }

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() == null) {
            em.persist(bookComment);
            return bookComment;
        } else {
            return em.merge(bookComment);
        }
    }

    @Override
    public Optional<BookComment> findById(long id) {
        return Optional.ofNullable(em.find(BookComment.class, id));
    }

    @Override
    public void delete(BookComment bookComment) {
        em.remove(bookComment);
    }

}
