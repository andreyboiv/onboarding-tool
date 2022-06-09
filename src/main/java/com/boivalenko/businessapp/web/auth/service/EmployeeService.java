package com.boivalenko.businessapp.web.auth.service;

import com.boivalenko.businessapp.web.auth.entity.Employee;
import com.boivalenko.businessapp.web.auth.repository.EmployeeRepository;
import com.boivalenko.businessapp.web.base.IBaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeService implements IBaseService<Employee> {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ResponseEntity<Employee> save(Employee employee) {
        return null;
    }

    @Override
    public ResponseEntity<Employee> update(Employee obj) {
        return null;
    }

    @Override
    public ResponseEntity<Employee> deleteById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Employee> findById(Long id) {
        if (id == 0) {
            return new ResponseEntity("ID darf nicht 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.employeeRepository.existsById(id) == false) {
            return new ResponseEntity("ID=" + id + " nicht gefunden",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        Employee employee = this.employeeRepository.findById(id).get();
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<List<Employee>> findAll() {
        return null;
    }

    public ResponseEntity<Employee> save(Employee employee, PasswordEncoder passwordEncoder) {
        if (employee.getId() != null) {
            return new ResponseEntity("ID wird automatisch generiert. Man muss das nicht eingeben",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (employee.getLogin() == null || employee.getLogin().isEmpty() || employee.getLogin().toLowerCase().contains("null")) {
            return new ResponseEntity("LOGIN darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (employee.getPassword() == null || employee.getPassword().isEmpty() || employee.getPassword().toLowerCase().contains("null")) {
            return new ResponseEntity("PASSWORD darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (employee.getEmail() == null || employee.getEmail().isEmpty() || employee.getEmail().toLowerCase().contains("null")) {
            return new ResponseEntity("EMAIL darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.employeeRepository.existsEmployeeByLogin(employee.getLogin())) {
            return new ResponseEntity("Es existiert ein Employee mit dem Login",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(employee.getEmail())) {
            return new ResponseEntity("Es existiert ein Employee mit der E-mail",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        //Einweg-Hash-Passwort-Encoder : Bcrypt
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        this.employeeRepository.save(employee);

        return ResponseEntity.ok().build();
    }
}