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
Kategorie von Tasks eines Employees
 */

@Entity
@Table(name = "category", schema = "onboarding", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
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
