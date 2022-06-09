package com.boivalenko.businessapp.web.auth.repository;

import com.boivalenko.businessapp.web.auth.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsEmployeeByLogin(String login);

    boolean existsEmployeeByEmailEqualsIgnoreCase(String email);
}
