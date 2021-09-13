package ru.otus.homework.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CORE_USERACCOUNT")
public class UserAccount {

    @Id
    @Column(name = "USERACCOUNTID")
    @SequenceGenerator(name = "CORE_USERACCOUNT_SEQ", sequenceName = "CORE_USERACCOUNT_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CORE_USERACCOUNT_SEQ" )
    private Long id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "STATUS")
    private String status;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 5)
    @ManyToMany(targetEntity = UserRole.class, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(name = "CORE_ROLEACCOUNT", joinColumns = @JoinColumn(name = "USERACCOUNTID"),
            inverseJoinColumns = @JoinColumn(name = "ROLEID")
    )
    private List<UserRole> userRoles;



}
