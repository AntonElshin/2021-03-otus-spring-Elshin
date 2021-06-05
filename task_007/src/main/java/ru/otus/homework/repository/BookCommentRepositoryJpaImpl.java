package ru.otus.homework.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
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
        if (bookComment.getId() <= 0) {
            em.persist(bookComment);
            return bookComment;
        } else {
            return em.merge(bookComment);
        }
    }

    @Override
    public Optional<BookComment> findById(long id) {
        TypedQuery<BookComment> query = em.createQuery("select bc from BookComment bc where bc.id = :id", BookComment.class);
        query.setParameter("id", id);
        return Optional.of(query.getSingleResult());
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete " +
                "from BookComment " +
                "where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<BookComment> findAllByBookId(long bookId) {
        TypedQuery<BookComment> query = em.createQuery("select bc from BookComment bc where bc.book.id = :bookId", BookComment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }
}
