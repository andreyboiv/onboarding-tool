package com.boivalenko.businessapp.web.backend.entity;

import com.boivalenko.businessapp.web.backend.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

/*
Alle Rechte, die zu einem Employee zugewiesen werden können
*/

@Entity
@Table(name = "powers", schema = "teamtasksplanning", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Powers extends BaseEntity {

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_powers",
            joinColumns = @JoinColumn(name = "power_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> employeesToPowers;

    @Override
    public String toString() {
        return "Powers{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
