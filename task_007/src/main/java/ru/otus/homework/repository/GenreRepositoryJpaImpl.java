package ru.otus.homework.repository;

import liquibase.util.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpaImpl implements GenreRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(g) from Genre g", Long.class);
        return query.getSingleResult();
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() <= 0) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public Optional<Genre> findById(long id) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.id = :id", Genre.class);
        query.setParameter("id", id);
        return Optional.of(query.getSingleResult());
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete " +
                "from Genre " +
                "where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public List<Genre> findByParamsEqualsIgnoreCase(String name) {

        String queryStr = "select g from Genre g where 1=1";

        if(name != null && !name.isEmpty()) {
            queryStr += " and lower(name) = :name";
        }

        return prepareConditions(queryStr, name).getResultList();

    }

    @Override
    public List<Genre> findByParamsLikeIgnoreCase(String name) {

        String queryStr = "select g from Genre g where 1=1";

        if(name != null && !name.isEmpty()) {
            name = "%" + name + "%";
            queryStr += " and lower(name) like :name";
        }

        return prepareConditions(queryStr, name).getResultList();

    }

    @Override
    public List<Genre> findByIds(List<Long> genreIds) {

        String queryStr = "select g from Genre g where g.id in :ids";

        TypedQuery<Genre> query = em.createQuery(queryStr,
                Genre.class);
        query.setParameter("ids", genreIds);
        return query.getResultList();
    }

    private TypedQuery<Genre> prepareConditions(String queryStr, String name) {

        TypedQuery<Genre> query = em.createQuery(queryStr,
                Genre.class);
        if(name != null && !name.isEmpty()) {
            query.setParameter("name", name.toLowerCase());
        }

        return query;

    }
}
