package ru.otus.homework.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REF_REFGROUP")
public class ReferenceGroupEntity {

    @Id
    @Column(name = "REFGROUPID")
    @SequenceGenerator( name = "REF_REFGROUP_SEQ", sequenceName = "REF_REFGROUP_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REF_REFGROUP_SEQ")
    private Long id;

    @Column(name = "PARENTGROUPID")
    private Long parentId;

    @Column(name = "GROUPNAME")
    private String name;

    @Column(name = "GROUPSYSNAME")
    private String sysname;

    @Column(name = "DESCRIPTION")
    private String description;

}
