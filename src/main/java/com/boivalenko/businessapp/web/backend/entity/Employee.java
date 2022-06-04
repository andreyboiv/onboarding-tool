package com.boivalenko.businessapp.web.backend.entity;

import com.boivalenko.businessapp.web.backend.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/*
Employee -
eine Hauptklasse,
mit denen alle andere Klassen verbunden sind
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
}
