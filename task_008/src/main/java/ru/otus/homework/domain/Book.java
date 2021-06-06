package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LIB_BOOKS")
@NamedEntityGraph(name = "book-comments-entity-graph",
        attributeNodes = {@NamedAttributeNode("comments")})
public class Book {
    @Id
    @Column(name = "BOOKID")
    @SequenceGenerator( name = "LIB_BOOKS_SEQ", sequenceName = "LIB_BOOKS_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIB_BOOKS_SEQ" )
    private long id;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "ISBN")
    private String isbn;
    @Column(name = "DESCRIPTION")
    private String description;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 5)
    @ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "LIB_BOOKAUTHORLINKS", joinColumns = @JoinColumn(name = "BOOKID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORID"))
    private List<Author> authors;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 5)
    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "LIB_BOOKGENRELINKS", joinColumns = @JoinColumn(name = "BOOKID"),
            inverseJoinColumns = @JoinColumn(name = "GENREID"))
    private List<Genre> genres;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    private List<BookComment> comments;

    public Book(String title, String isbn, String description, List<Author> authors, List<Genre> genres, List<BookComment> comments) {
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.authors = authors;
        this.genres = genres;
    }
}
