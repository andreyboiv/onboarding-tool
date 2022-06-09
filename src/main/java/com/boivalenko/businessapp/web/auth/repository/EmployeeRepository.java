package com.boivalenko.businessapp.web.auth.repository;

import com.boivalenko.businessapp.web.auth.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsEmployeeByLogin(String login);

    boolean existsEmployeeByEmailEqualsIgnoreCase(String email);

    Optional<Employee> findEmployeeByLogin(String login);

    Optional<Employee> findEmployeeByEmail(String email);
}
