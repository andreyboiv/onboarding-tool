package com.boivalenko.businessapp.web.backend.entity;

import com.boivalenko.businessapp.web.backend.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


/*
Kategorie von Tasks eines Employees
 */

@Entity
@Table(name = "category", schema = "teamtasksplanning", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category extends BaseEntity {

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "completed_count", nullable = true)
    private Long completedCount;

    @Basic
    @Column(name = "uncompleted_count", nullable = true)
    private Long uncompletedCount;

    @Basic
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

}
