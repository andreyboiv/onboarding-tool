package com.boivalenko.businessapp.teamtasksplanning.web.auth.repository;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsEmployeeByLoginEqualsIgnoreCase(String login);

    boolean existsEmployeeByEmailEqualsIgnoreCase(String email);

    Optional<Employee> findEmployeeByLogin(String login);

    Optional<Employee> findEmployeeByEmail(String email);

    Optional<Employee> findEmployeeByActivity_Uuid(String uuid);

    //Es wird die Anzahl von geänderten Datensätzen zurückgegeben. Es muss 1 gegeben werden
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.password = :password WHERE e.login = :login")
    int updatePassword(@Param("login") String login, @Param("password") String password);
}
