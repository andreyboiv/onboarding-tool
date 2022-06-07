package com.boivalenko.businessapp.web.entity;

import com.boivalenko.businessapp.web.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

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

    // wird konvertiert
    // von boolean to numeric (true = 1, false = 0)
    @Basic
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean completed = false;

    @Basic
    @Column(name = "task_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date taskDate = new Date();

    // Ein Task kann nur eine Kategorie haben
    // (andererseits kann dieselbe Kategorie
    // in mehreren Tasks verwendet werden)
    // Default - EAGER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference(value = "categoryBackReference")
    private Category category;

    // Ein Task kann nur eine Priorität haben
    // (andererseits kann dieselbe Priorität
    // in mehreren Tasks verwendet werden)
    // Default - EAGER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference(value = "priorityBackReference")
    private Priority priority;

    // Foreign Key
    // Default - EAGER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonBackReference(value = "employeesToTaskBackReference")
    private Employee employeesToTask;

    @Override
    public String toString() {
        return this.title;
    }

}
