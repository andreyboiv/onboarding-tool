package com.boivalenko.businessapp.teamtasksplanning.web.auth.entity;

import com.boivalenko.businessapp.teamtasksplanning.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

/*
Alle Rechte, die zu einem EmployeeVm zugewiesen werden können
*/

@Entity
@Table(name = "powers", schema = "teamtasksplanning", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@DynamicInsert
@DynamicUpdate
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Powers extends BaseEntity {

    @Basic
    @Column(name = "name", nullable = false, length = -1, updatable = false)
    private String name;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_powers",
            joinColumns = @JoinColumn(name = "power_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    @JsonBackReference
    private Set<Employee> employeesToPowers;

    @Override
    public String toString() {
        return "Powers{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
