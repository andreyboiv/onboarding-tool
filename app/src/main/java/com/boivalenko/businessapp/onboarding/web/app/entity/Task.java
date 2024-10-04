package com.boivalenko.businessapp.onboarding.web.app.entity;

import com.boivalenko.businessapp.onboarding.web.auth.entity.Employee;
import com.boivalenko.businessapp.onboarding.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

/*
Task -
eine Hauptklasse,
mit denen alle andere Klassen verbunden sind
 */

@Entity
@Table(name = "task", schema = "public", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task extends BaseEntity {

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    // wird konvertiert
    // von boolean to numeric (true = 1, false = 0)
    @Basic
    @Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
    private Boolean completed;

    @Basic
    @Column(name = "task_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date taskDate;

    // Ein Task kann nur eine Kategorie haben
    // (andererseits kann dieselbe Kategorie
    // in mehreren Tasks verwendet werden)
    // Default - EAGER
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", updatable = false, nullable = false, referencedColumnName = "id")
    @JsonBackReference(value = "categoryBackReference")
    private Category category;

    // Foreign Key
    // Default - EAGER
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonBackReference(value = "employeesToTaskBackReference")
    private Employee employeesToTask;

    @Override
    public String toString() {
        return this.title;
    }
}
