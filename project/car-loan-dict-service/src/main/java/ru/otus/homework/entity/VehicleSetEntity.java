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
@Table(name = "REF_VEHICLESET")
public class VehicleSetEntity {

    @Id
    @Column(name = "VEHICLESETID")
    @SequenceGenerator( name = "REF_VEHICLESET_SEQ", sequenceName = "REF_VEHICLESET_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REF_VEHICLESET_SEQ")
    private Long id;

    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "VEHICLEMODELID")
    private VehicleModelEntity model;

    @OneToOne
    @JoinColumn(name = "BODYTYPEID")
    private ReferenceItemEntity body;

    @OneToOne
    @JoinColumn(name = "ENGINESIZEID")
    private ReferenceItemEntity engineSize;

    @OneToOne
    @JoinColumn(name = "ENGINETYPEID")
    private ReferenceItemEntity engineType;

    @OneToOne
    @JoinColumn(name = "POWERID")
    private ReferenceItemEntity power;

    @OneToOne
    @JoinColumn(name = "TRANSMISSIONID")
    private ReferenceItemEntity transmission;

    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 5)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "set", cascade = CascadeType.REMOVE)
    private List<VehicleSetYearEntity> years;

    public VehicleSetEntity(Long id, VehicleModelEntity model, ReferenceItemEntity body, ReferenceItemEntity engineSize, ReferenceItemEntity engineType, ReferenceItemEntity power, ReferenceItemEntity transmission) {
        this.id = id;
        this.model = model;
        this.body = body;
        this.engineSize = engineSize;
        this.engineType = engineType;
        this.power = power;
        this.transmission = transmission;
    }
}
