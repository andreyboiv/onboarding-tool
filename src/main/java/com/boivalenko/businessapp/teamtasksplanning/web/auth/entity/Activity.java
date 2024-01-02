package com.boivalenko.businessapp.teamtasksplanning.web.auth.entity;


import com.boivalenko.businessapp.teamtasksplanning.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

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
@EqualsAndHashCode
@DynamicInsert
@DynamicUpdate
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity extends BaseEntity {

    // wird ein mal mit trigger eingesetzt.
    // deswegen braucht man kein update machen -> updatable = false
    // uuid ist für die Registration eines Employess gedacht
    @Basic
    @Column(name = "uuid", nullable = false, updatable = false, length = -1)
    private String uuid;

    // wird konvertiert
    // von boolean to numeric (true = 1, false = 0)
    // wird erst nach Bestätigung der Aktivierung durch
    // den Benutzer als "true" eingesetzt
    @Basic
    @Column(name = "activated", nullable = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean activated;

    // Daten von der Tabelle EmployeeVm braucht man
    // an dieser Stelle nicht immer,
    // deswegen - LAZY
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Employee employeeToActivity;

}
