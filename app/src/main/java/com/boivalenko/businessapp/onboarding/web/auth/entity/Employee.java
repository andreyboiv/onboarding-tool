package com.boivalenko.businessapp.onboarding.web.auth.entity;

import com.boivalenko.businessapp.onboarding.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.HashSet;
import java.util.Set;

/*
Employee -
eine Hauptklasse,
mit denen alle andere Klassen verbunden sind.
Die credentials eines Employees
 */

@Entity
@Table(name = "employee", schema = "onboarding", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
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

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "employeesToPowers", fetch = FetchType.EAGER)
    private Set<Powers> powers = new HashSet<>();

    /*
    @OneToMany(mappedBy = "employeesToTask", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "employeesToCategory",fetch = FetchType.LAZY)
    private List<Category> categories;
*/

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "employeeToActivity",fetch = FetchType.LAZY, optional = false)
    private Activity activity;

    @Override
    public String toString() {
        return "EmployeeVm{" +
                "login='" + this.login + '\'' +
                '}';
    }
}
