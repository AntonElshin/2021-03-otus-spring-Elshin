package ru.otus.homework.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REF_VEHICLESETYEAR")
public class VehicleSetYearEntity {

    @Id
    @Column(name = "VEHICLESETYEARID")
    @SequenceGenerator( name = "REF_VEHICLESETYEAR_SEQ", sequenceName = "REF_VEHICLESETYEAR_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REF_VEHICLESETYEAR_SEQ")
    private Long id;

    @ManyToOne(targetEntity = VehicleSetEntity.class, cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLESETID")
    private VehicleSetEntity set;

    @OneToOne
    @JoinColumn(name = "YEARID")
    private ReferenceItemEntity year;

    @Column(name = "PRICE")
    private BigDecimal price;

}
