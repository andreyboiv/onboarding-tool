package com.boivalenko.businessapp.teamtasksplanning.web.app.entity;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
@EqualsAndHashCode
@DynamicInsert
@DynamicUpdate
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category extends BaseEntity {

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    // updatable muss "false" sein,
    // weil die Werte von einem Trigger gesteuert werden
    @Basic
    @Column(name = "completed_count", insertable = false, updatable = false, nullable = true)
    private Long completedCount = 0L;

    // updatable muss "false" sein,
    // weil die Werte von einem Trigger gesteuert werden
    @Basic
    @Column(name = "uncompleted_count", insertable = false, updatable = false, nullable = true)
    private Long uncompletedCount = 0L;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id",referencedColumnName = "id", nullable = false, updatable = false)
    @JsonBackReference
    private Employee employeesToCategory;

    @Override
    public String toString() {
        return this.title;
    }
}
