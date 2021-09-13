package ru.otus.homework.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REF_REFITEM")
public class ReferenceItemEntity {

    @Id
    @Column(name = "REFITEMID")
    @SequenceGenerator( name = "REF_REFITEM_SEQ", sequenceName = "REF_REFITEM_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REF_REFITEM_SEQ")
    private Long id;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "REFERENCEID")
    private ReferenceEntity reference;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BRIEF")
    private String brief;

    @Column(name = "DESCRIPTION")
    private String description;

}
