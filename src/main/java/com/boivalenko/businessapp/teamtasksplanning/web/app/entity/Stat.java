package com.boivalenko.businessapp.teamtasksplanning.web.app.entity;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


/*
Gesamte Statistik
von abgeschlossenen und nicht abgeschlossenen Tasks eines Employees.
Dabei ist es nicht wichtig welche Kategorien die Tasks haben.
*/

@Entity
@Table(name = "stat", schema = "teamtasksplanning", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "employee_id", nullable = false, updatable = false)
    @JsonBackReference
    private Employee employeeToStat;

}
