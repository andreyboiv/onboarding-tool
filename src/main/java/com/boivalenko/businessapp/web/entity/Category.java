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

    // updatable muss "false" sein,
    // weil die Werte von einem Trigger gesteuert werden
    @Basic
    @Column(name = "completed_count", updatable = false, nullable = true)
    private Long completedCount = 0L;

    // updatable muss "false" sein,
    // weil die Werte von einem Trigger gesteuert werden
    @Basic
    @Column(name = "uncompleted_count", updatable = false, nullable = true)
    private Long uncompletedCount = 0L;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id",referencedColumnName = "id", nullable = false, updatable = false)
    @JsonBackReference
    private Employee employeesToCategory;

    @Override
    public String toString() {
        return this.title;
    }

}
