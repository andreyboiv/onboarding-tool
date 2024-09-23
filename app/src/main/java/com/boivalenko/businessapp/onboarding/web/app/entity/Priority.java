package com.boivalenko.businessapp.onboarding.web.app.entity;

import com.boivalenko.businessapp.onboarding.web.auth.entity.Employee;
import com.boivalenko.businessapp.onboarding.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/*
Priorität von Tasks eines Employees
 */

@Entity
@Table(name = "priority", schema = "onboarding", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Priority extends BaseEntity {

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "color", nullable = true, length = -1)
    private String color;

    // Daten von der Tabelle EmployeeVm braucht man
    // an dieser Stelle nicht immer,
    // deswegen - LAZY
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonBackReference
    private Employee employeesToPriority;

    @Override
    public String toString() {
        return this.title;
    }
}
