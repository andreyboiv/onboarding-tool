package com.boivalenko.businessapp.web.backend.entity;

import com.boivalenko.businessapp.web.backend.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/*
Alle Benutzeraktivitäten
(Kontoaktivierung, sonstiges
Aktionen nach Bedarf)
*/

@Entity
@Table(name = "activity", schema = "teamtasksplanning", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity extends BaseEntity {

    @Basic
    @Column(name = "uuid", nullable = false, length = -1)
    private String uuid;

    @Basic
    @Column(name = "activated", nullable = false)
    private Short activated;

    @Basic
    @Column(name = "employee_id", nullable = true)
    private Long employeeId;

}
