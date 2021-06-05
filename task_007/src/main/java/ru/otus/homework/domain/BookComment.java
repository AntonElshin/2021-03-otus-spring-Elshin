package ru.otus.homework.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LIB_BOOKCOMMENTS")
public class BookComment {

    @Id
    @Column(name = "COMMENTID")
    @SequenceGenerator( name = "LIB_BOOKCOMMENTS_SEQ", sequenceName = "LIB_BOOKCOMMENTS_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIB_BOOKCOMMENTS_SEQ" )
    private long id;

    @ManyToOne(targetEntity = Book.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKID")
    private Book book;

    @Column(name = "TEXT", nullable = false)
    private String text;

    public BookComment(Book book, String text) {
        this.book = book;
        this.text = text;
    }
}
