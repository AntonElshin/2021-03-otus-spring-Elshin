package ru.otus.homework.entity;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CORE_USERROLE")
public class UserRole {

    @Id
    @Column(name = "ROLEID")
    @SequenceGenerator( name = "CORE_USERROLE_SEQ", sequenceName = "CORE_USERROLE_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CORE_USERROLE_SEQ" )
    private Long id;

    @Column(name = "ROLENAME")
    private String name;

    @Column(name = "ROLESYSNAME")
    private String sysname;

    @Column(name = "DESCRIPTION")
    private String description;

}
