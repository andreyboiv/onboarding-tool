package com.boivalenko.businessapp.onboarding.web.auth.entity;

import com.boivalenko.businessapp.onboarding.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.Set;

/*
Alle Rechte, die zu einem EmployeeVm zugewiesen werden können
*/

@Entity
@Table(name = "powers", schema = "public", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
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
