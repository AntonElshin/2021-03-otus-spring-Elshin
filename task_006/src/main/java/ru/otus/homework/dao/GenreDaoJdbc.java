package ru.otus.homework.dao;

import liquibase.util.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(*) from lib_genres", Integer.class);
    }

    @Override
    public void insert(long id, Genre genre) {
        if(id == 0) {
            namedParameterJdbcOperations.update("insert into lib_genres (name, description) values (:name, :description)",
                    Map.of("name", genre.getName(), "description", genre.getDescription()));
        }
        else {
            namedParameterJdbcOperations.update("insert into lib_genres (genreid, name, description) values (:genreid, :name, :description)",
                    Map.of("genreid", id,"name", genre.getName(), "description", genre.getDescription()));
        }

    }

    @Override
    public void update(long id, Genre genre) {
        namedParameterJdbcOperations.update("update lib_genres set name = :name, description = :description where genreid = :id",
                Map.of("id", id , "name", genre.getName(), "description", genre.getDescription()));
    }

    @Override
    public Genre getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select genreid, name, description from lib_genres where genreid = :id",
                params,
                new GenreMapper()
        );
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from lib_genres where genreid = :id", params
        );
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.getJdbcOperations().query("select genreid, name, description from lib_genres", new GenreMapper());
    }

    @Override
    public List<Genre> getByParamsEqualsIgnoreCase(String name) {

        String query = "select genreid, name, description from lib_genres where 1=1";

        if(name != null && !name.isEmpty()) {
            query += " and lower(name) = '" + name.toLowerCase() + "'";
        }

        List<Genre> genres = namedParameterJdbcOperations.getJdbcOperations().query(query, new GenreMapper());

        return genres;
    }

    @Override
    public List<Genre> getByParamsLikeIgnoreCase(String name) {

        String query = "select genreid, name, description from lib_genres where 1=1";

        if(name != null && !name.isEmpty()) {
            query += " and lower(name) like '%" + name.toLowerCase() + "%'";
        }

        List<Genre> genres = namedParameterJdbcOperations.getJdbcOperations().query(query, new GenreMapper());

        return genres;
    }

    @Override
    public List<Genre> getByIds(List<String> genreIds) {

        String query = "select genreid, name, description from lib_genres where genreid in (" + StringUtils.join(genreIds, ", ") + ")";

        List<Genre> genres = namedParameterJdbcOperations.getJdbcOperations().query(query, new GenreMapper());

        return genres;
    }

    public Integer getLinkedBookCount(long id) {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(bookid) from lib_bookgenrelinks where genreid = " + id, Integer.class);
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("genreid");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            return new Genre(id, name, description);
        }
    }
}
