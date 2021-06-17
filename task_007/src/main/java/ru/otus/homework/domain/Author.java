package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LIB_AUTHORS")
public class Author {
    @Id
    @Column(name = "AUTHORID")
    @SequenceGenerator( name = "LIB_AUTHORS_SEQ", sequenceName = "LIB_AUTHORS_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIB_AUTHORS_SEQ" )
    private Long id;

    @Column(name = "LASTNAME", nullable = false)
    private String lastName;

    @Column(name = "FIRSTNAME", nullable = false)
    private String firstName;

    @Column(name = "MIDDLENAME")
    private String middleName;

    public Author(String lastName, String firstName, String middleName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }
}
