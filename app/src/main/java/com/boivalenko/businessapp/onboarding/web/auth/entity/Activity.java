package com.boivalenko.businessapp.onboarding.web.auth.entity;


import com.boivalenko.businessapp.onboarding.web.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/*
Alle Benutzeraktivitäten
(Kontoaktivierung, sonstiges
Aktionen nach Bedarf)
*/

@Entity
@Table(name = "activity", schema = "public", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Cacheable(value = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity extends BaseEntity {

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
    @Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
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
