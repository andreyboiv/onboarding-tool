package com.boivalenko.businessapp.web.auth.repository;

import com.boivalenko.businessapp.web.auth.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Modifying // wenn die Anfrage die Daten ändert - es ist ratsam, diese Annotation hinzuzufügen
    @Transactional // wenn die Anfrage die Daten ändert - es ist ratsam, diese Annotation hinzuzufügen
    @Query("UPDATE Activity a SET a.activated = true WHERE a.uuid=:uuid")
    int activate(@Param("uuid") String uuid); // gibt int zurück (wie viele Datensätze aktualisiert) - sollte immer 1 zurückgegeben werden

    @Modifying // wenn die Anfrage die Daten ändert - es ist ratsam, diese Annotation hinzuzufügen
    @Transactional // wenn die Anfrage die Daten ändert - es ist ratsam, diese Annotation hinzuzufügen
    @Query("UPDATE Activity a SET a.activated = false WHERE a.uuid=:uuid")
    int deActivate(@Param("uuid") String uuid); // gibt int zurück (wie viele Datensätze aktualisiert) - sollte immer 1 zurückgegeben werden

    Optional<Activity> findActivityByUuid(String uuid);
}
