package com.boivalenko.businessapp.web.entity;

import com.boivalenko.businessapp.web.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
    private Boolean activated = false;

    // Daten von der Tabelle Employee braucht man
    // an dieser Stelle nicht immer,
    // deswegen - LAZY
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Employee employeeToActivity;

}
