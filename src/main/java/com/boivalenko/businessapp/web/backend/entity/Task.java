package com.boivalenko.businessapp.web.backend.entity;

import com.boivalenko.businessapp.web.backend.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Timestamp;

/*
Task -
eine Hauptklasse,
mit denen alle andere Klassen verbunden sind
 */

@Entity
@Table(name = "task", schema = "teamtasksplanning", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task extends BaseEntity {

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "completed", nullable = false)
    private Short completed;

    @Basic
    @Column(name = "task_date", nullable = true)
    private Timestamp taskDate;

    @Basic
    @Column(name = "category_id", nullable = true)
    private Long categoryId;

    @Basic
    @Column(name = "priority_id", nullable = true)
    private Long priorityId;

    @Basic
    @Column(name = "employee_id", nullable = true)
    private Long employeeId;

}
