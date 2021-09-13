package ru.otus.homework.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REF_VEHICLEMODEL")
public class VehicleModelEntity {

    @Id
    @Column(name = "VEHICLEMODELID")
    @SequenceGenerator( name = "REF_VEHICLEMODEL_SEQ", sequenceName = "REF_VEHICLEMODEL_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REF_VEHICLEMODEL_SEQ")
    private Long id;

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "VEHICLEBRANDID")
    private VehicleBrandEntity brand;

    @Column(name = "MODELNAME")
    private String name;

    @OneToOne
    @JoinColumn(name = "KINDID")
    private ReferenceItemEntity kind;

    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 5)
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "REF_MODELOWNFORMLINK",
            joinColumns = @JoinColumn(name = "VEHICLEMODELID"),
            inverseJoinColumns = @JoinColumn(name = "OWNFORMID")
    )
    private List<ReferenceItemEntity> ownForms;

}
