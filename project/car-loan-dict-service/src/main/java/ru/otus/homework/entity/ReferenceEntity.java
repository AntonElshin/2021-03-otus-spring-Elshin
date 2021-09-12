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
@Table(name = "REF_REFERENCE")
public class ReferenceEntity {

    @Id
    @Column(name = "REFERENCEID")
    @SequenceGenerator( name = "REF_REFERENCE_SEQ", sequenceName = "REF_REFERENCE_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REF_REFERENCE_SEQ")
    private Long id;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "REFGROUPID")
    private ReferenceGroupEntity group;

    @Column(name = "REFERENCENAME")
    private String name;

    @Column(name = "REFSYSNAME")
    private String sysname;

    @Column(name = "DESCRIPTION")
    private String description;

}
