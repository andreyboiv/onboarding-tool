package com.boivalenko.businessapp.web.backend.entity;

import com.boivalenko.businessapp.web.backend.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


/*
Gesamte Statistik
von abgeschlossenen und nicht abgeschlossenen Tasks eines Employees
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
    @Basic
    @Column(name = "completed_total", nullable = true)
    private Long completedTotal;

    @Basic
    @Column(name = "uncompleted_total", nullable = true)
    private Long uncompletedTotal;

    @Basic
    @Column(name = "employee_id", nullable = true)
    private Long employeeId;

}
