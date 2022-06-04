package com.boivalenko.businessapp.web.entity;

import com.boivalenko.businessapp.web.entity.base.BaseEntity;
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
    @Column(name = "completed_total", updatable = false, nullable = true)
    private Long completedTotal;

    // updatable muss "false" sein,
    // weil die Werte von einem Trigger gesteuert werden
    @Basic
    @Column(name = "uncompleted_total", updatable = false, nullable = true)
    private Long uncompletedTotal;

    // Default - EAGER
    @OneToOne
    @MapsId
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employeesToStat;

}
