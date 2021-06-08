package ru.otus.homework.repository;

import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpaImpl implements AuthorRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(a) from Author a", Long.class);
        return query.getSingleResult();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public void delete(Author author) {
        em.remove(author);
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public List<Author> findByParamsEqualsIgnoreCase(String lastName, String firstName, String middleName) {

        String queryStr = "select a from Author a where 1=1";

        if(lastName != null && !lastName.isEmpty()) {
            queryStr += " and lower(a.lastName) = :lastName";
        }
        if(firstName != null && !firstName.isEmpty()) {
            queryStr += " and lower(a.firstName) = :firstName";
        }
        if(middleName != null && !middleName.isEmpty()) {
            queryStr += " and lower(middleName) = :middleName";
        }

        return prepareConditions(queryStr, lastName, firstName, middleName).getResultList();
    }

    @Override
    public List<Author> findByParamsLikeIgnoreCase(String lastName, String firstName, String middleName) {

        String queryStr = "select a from Author a where 1=1";

        if(lastName != null && !lastName.isEmpty()) {
            lastName = "%" + lastName + "%";
            queryStr += " and lower(a.lastName) like :lastName";
        }
        if(firstName != null && !firstName.isEmpty()) {
            firstName = "%" + firstName + "%";
            queryStr += " and lower(a.firstName) like :firstName";
        }
        if(middleName != null && !middleName.isEmpty()) {
            middleName = "%" + middleName + "%";
            queryStr += " and lower(a.middleName) like :middleName";
        }

        return prepareConditions(queryStr, lastName, firstName, middleName).getResultList();

    }

    @Override
    public List<Author> findByIds(List<Long> authorIds) {

        String queryStr = "select a from Author a where a.id in :ids";

        TypedQuery<Author> query = em.createQuery(queryStr,
                Author.class);
        query.setParameter("ids", authorIds);
        return query.getResultList();
    }

    private TypedQuery<Author> prepareConditions(String queryStr, String lastName, String firstName, String middleName) {

        TypedQuery<Author> query = em.createQuery(queryStr,
                Author.class);
        if(lastName != null && !lastName.isEmpty()) {
            query.setParameter("lastName", lastName.toLowerCase());
        }
        if(firstName != null && !firstName.isEmpty()) {
            query.setParameter("firstName", firstName.toLowerCase());
        }
        if(middleName != null && !middleName.isEmpty()) {
            query.setParameter("middleName", middleName.toLowerCase());
        }

        return query;

    }

}
