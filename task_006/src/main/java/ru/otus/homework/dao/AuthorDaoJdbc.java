package ru.otus.homework.dao;

import liquibase.util.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.dao.ext.BookAuthor;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(*) from lib_authors", Integer.class);
    }

    @Override
    public void insert(long id, Author author) {

        if(id == 0) {
            namedParameterJdbcOperations.update("insert into lib_authors (lastname, firstname, middlename) values (:lastname, :firstname, :middlename)",
                    Map.of("lastname", author.getLastName(), "firstname", author.getFirstName(), "middlename", author.getMiddleName()));
        }
        else {
            namedParameterJdbcOperations.update("insert into lib_authors (authorid, lastname, firstname, middlename) values (:authorid, :lastname, :firstname, :middlename)",
                    Map.of("authorid", id,"lastname", author.getLastName(), "firstname", author.getFirstName(), "middlename", author.getMiddleName()));
        }
    }

    @Override
    public void update(long id, Author author) {
        namedParameterJdbcOperations.update("update lib_authors set lastname = :lastname, firstname = :firstname, middlename = :middlename where authorid = :id",
                Map.of("id", id , "lastname", author.getLastName(),"firstname", author.getFirstName(),"middlename", author.getMiddleName()));
    }

    @Override
    public Author getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select authorid, lastname, firstname, middlename from lib_authors where authorid = :id",
                params,
                new AuthorDaoJdbc.AuthorMapper()
        );
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from lib_authors where authorid = :id", params
        );
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.getJdbcOperations().query("select authorid, lastname, firstname, middlename from lib_authors", new AuthorDaoJdbc.AuthorMapper());
    }

    @Override
    public List<Author> getByParamsEqualsIgnoreCase(String lastName, String firstName, String middleName) {

        if(lastName != null && !lastName.isEmpty()) {
            lastName = lastName.toLowerCase();
        }
        if(firstName != null && !firstName.isEmpty()) {
            firstName = firstName.toLowerCase();
        }
        if(middleName != null && !middleName.isEmpty()) {
            middleName = middleName.toLowerCase();
        }

        Map<String, Object> params = new HashMap<>();

        String query = "select authorid, lastname, firstname, middlename from lib_authors where 1=1";

        if(lastName != null && !lastName.isEmpty()) {
            query += " and lower(lastName) = '" + lastName.toLowerCase() + "'";
        }
        if(firstName != null && !firstName.isEmpty()) {
            query += " and lower(firstName) = '" + firstName.toLowerCase() + "'";
        }
        if(middleName != null && !middleName.isEmpty()) {
            query += " and lower(middleName) = '" + middleName.toLowerCase() + "'";
        }

        List<Author> authors = namedParameterJdbcOperations.getJdbcOperations().query(query, new AuthorMapper());

        return authors;

    }

    @Override
    public List<Author> getByParamsLikeIgnoreCase(String lastName, String firstName, String middleName) {

        if(lastName != null && !lastName.isEmpty()) {
            lastName = lastName.toLowerCase();
        }
        if(firstName != null && !firstName.isEmpty()) {
            firstName = firstName.toLowerCase();
        }
        if(middleName != null && !middleName.isEmpty()) {
            middleName = middleName.toLowerCase();
        }

        String query = "select authorid, lastname, firstname, middlename from lib_authors where 1=1";

        if(lastName != null && !lastName.isEmpty()) {
            query += " and lower(lastName) like '%" + lastName.toLowerCase()  + "%'";
        }
        if(firstName != null && !firstName.isEmpty()) {
            query += " and lower(firstName) like '%" + firstName.toLowerCase()  + "%'";
        }
        if(middleName != null && !middleName.isEmpty()) {
            query += " and lower(middleName) like '%" + middleName.toLowerCase()  + "%'";
        }

        List<Author> authors = namedParameterJdbcOperations.getJdbcOperations().query(query, new AuthorMapper());

        return authors;

    }

    @Override
    public List<Author> getByIds(List<String> authorIds) {

        String query = "select authorid, lastname, firstname, middlename from lib_authors where authorid in (" + StringUtils.join(authorIds, ", ") + ")";

        List<Author> authors = namedParameterJdbcOperations.getJdbcOperations().query(query, new AuthorMapper());

        return authors;
    }

    public Integer getLinkedBookCount(long id) {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(bookid) from lib_bookauthorlinks where authorid = " + id, Integer.class);
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("authorid");
            String lastname = resultSet.getString("lastname");
            String firstname = resultSet.getString("firstname");
            String middlename = resultSet.getString("middlename");
            return new Author(id, lastname, firstname, middlename);
        }
    }

}
