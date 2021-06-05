package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LIB_GENRES")
public class Genre {
    @Id
    @Column(name = "GENREID")
    @SequenceGenerator( name = "LIB_GENRES_SEQ", sequenceName = "LIB_GENRES_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIB_GENRES_SEQ" )
    private long id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;

    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
