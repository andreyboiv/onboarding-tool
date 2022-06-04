package com.boivalenko.businessapp.web.entity;

import com.boivalenko.businessapp.web.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Employee -
eine Hauptklasse,
mit denen alle andere Klassen verbunden sind.
Die credentials eines Employees
 */

@Entity
@Table(name = "employee", schema = "teamtasksplanning", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Employee extends BaseEntity {

    @Basic
    @Column(name = "login", nullable = true, length = -1)
    private String login;

    @Basic
    @Column(name = "password", nullable = true, length = -1)
    private String password;

    @Basic
    @Column(name = "email", nullable = true, length = -1)
    private String email;

    @ManyToMany(mappedBy = "employeesToPowers", fetch = FetchType.EAGER)
    private Set<Powers> powers = new HashSet<>();

    @OneToMany(mappedBy = "employeesToTask", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "employeesToCategory",fetch = FetchType.LAZY)
    private List<Category> categories;

    @OneToMany(mappedBy = "employeesToPriority",fetch = FetchType.LAZY)
    private List<Priority> priorities;

    @OneToOne(mappedBy = "employeeToActivity",fetch = FetchType.LAZY, optional = false)
    private Activity activity;

    @OneToOne(mappedBy = "employeesToStat",fetch = FetchType.LAZY, optional = false)
    private Stat stat;

    @Override
    public String toString() {
        return "Employee{" +
                "login='" + this.login + '\'' +
                '}';
    }
}
