package com.boivalenko.businessapp.web.entity;

import com.boivalenko.businessapp.web.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/*
Priorität von Tasks eines Employees
 */

@Entity
@Table(name = "priority", schema = "teamtasksplanning", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Priority extends BaseEntity {

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "color", nullable = true, length = -1)
    private String color = "#99c1c4";

    // Daten von der Tabelle Employee braucht man
    // an dieser Stelle nicht immer,
    // deswegen - LAZY
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonBackReference
    private Employee employeesToPriority;

    @Override
    public String toString() {
        return this.title;
    }
}
