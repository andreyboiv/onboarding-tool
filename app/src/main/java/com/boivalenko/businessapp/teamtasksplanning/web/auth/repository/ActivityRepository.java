package com.boivalenko.businessapp.teamtasksplanning.web.auth.repository;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Activity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Modifying // wenn die Anfrage die Daten ändert - es ist ratsam, diese Annotation hinzuzufügen
    @Transactional // wenn die Anfrage die Daten ändert - es ist ratsam, diese Annotation hinzuzufügen
    @Query("UPDATE Activity a SET a.activated = true WHERE a.uuid=:uuid")
    int activate(@Param("uuid") String uuid); // gibt int zurück (wie viele Datensätze aktualisiert) - sollte immer 1 zurückgegeben werden

    @Modifying // wenn die Anfrage die Daten ändert - es ist ratsam, diese Annotation hinzuzufügen
    @Transactional // wenn die Anfrage die Daten ändert - es ist ratsam, diese Annotation hinzuzufügen
    @Query("UPDATE Activity a SET a.activated = false WHERE a.employeeToActivity.id=:employeeId")
    int deActivate(@Param("employeeId") Long employeeId); // gibt int zurück (wie viele Datensätze aktualisiert) - sollte immer 1 zurückgegeben werden

    Optional<Activity> findActivityByUuid(String uuid);

    Optional<Activity> findActivityById(Long id);

    Optional<Activity> findActivityByEmployeeToActivity_Id(Long employeeID);
}
