package com.boivalenko.businessapp.web.backend.entity;

import com.boivalenko.businessapp.web.backend.entity.base.BaseEntity;
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
    @Column(name = "color", nullable = false, length = -1)
    private String color;

    @Basic
    @Column(name = "employee_id", nullable = true)
    private Long employeeId;

}
