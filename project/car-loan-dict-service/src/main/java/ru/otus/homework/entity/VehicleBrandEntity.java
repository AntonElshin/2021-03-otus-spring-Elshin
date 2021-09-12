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
@Table(name = "REF_VEHICLEBRAND")
public class VehicleBrandEntity {

    @Id
    @Column(name = "VEHICLEBRANDID")
    @SequenceGenerator( name = "REF_VEHICLEBRAND_SEQ", sequenceName = "REF_VEHICLEBRAND_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REF_VEHICLEBRAND_SEQ")
    private Long id;

    @Column(name = "BRANDNAME")
    private String name;

    @OneToOne
    @JoinColumn(name = "PRODUCTIONKINDID")
    private ReferenceItemEntity productionKind;

}
