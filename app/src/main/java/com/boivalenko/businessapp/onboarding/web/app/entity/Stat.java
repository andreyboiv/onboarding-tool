package com.boivalenko.businessapp.onboarding.web.app.entity;

import com.boivalenko.businessapp.onboarding.web.auth.entity.Employee;
import com.boivalenko.businessapp.onboarding.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/*
Gesamte Statistik
von abgeschlossenen und nicht abgeschlossenen Tasks eines Employees.
Dabei ist es nicht wichtig welche Kategorien die Tasks haben.
*/

@Entity
@Table(name = "stat", schema = "onboarding", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Stat extends BaseEntity {

    // updatable muss "false" sein,
    // weil die Werte von einem Trigger gesteuert werden
    @Basic
    @Column(name = "completed_total", insertable = false, updatable = false, nullable = true)
    private Long completedTotal = 0L;

    // updatable muss "false" sein,
    // weil die Werte von einem Trigger gesteuert werden
    @Basic
    @Column(name = "uncompleted_total", insertable = false, updatable = false, nullable = true)
    private Long uncompletedTotal = 0L;

    // Default - EAGER
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "employee_id", nullable = false, updatable = false)
    @JsonBackReference
    private Employee employeeToStat;
}
