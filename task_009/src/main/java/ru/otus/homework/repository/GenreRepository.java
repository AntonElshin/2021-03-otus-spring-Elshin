package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.homework.domain.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByNameEqualsIgnoreCase(String name);

    List<Genre> findByNameContainingIgnoreCase(String name);

    @Query("select g from Genre g where g.id in :ids")
    List<Genre> findByIds(@Param("ids") List<Long> genreIds);

}
