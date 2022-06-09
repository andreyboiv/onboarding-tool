package com.boivalenko.businessapp.web.auth.service;

import com.boivalenko.businessapp.web.auth.entity.Employee;
import com.boivalenko.businessapp.web.auth.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private static final String EMAIL_REGEX_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private String error;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ResponseEntity<Employee> register(Employee employee, PasswordEncoder passwordEncoder) {

        if (isEmployeeValid(employee) == false) {
            return new ResponseEntity(getError(), HttpStatus.NOT_ACCEPTABLE);
        }

        //Einweg-Hash-Passwort-Encoder : Bcrypt
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        this.employeeRepository.save(employee);

        return ResponseEntity.ok().build();
    }


    private boolean isEmployeeValid(Employee employee) {

        if (employee.getId() != null) {
            this.error = "ID wird automatisch generiert. Man muss da nichts eingeben";
            return false;
        }

        if (employee.getLogin() == null || employee.getLogin().isEmpty() || employee.getLogin().toLowerCase().contains("null")) {
            this.error = "LOGIN darf weder NULL noch leer sein";
            return false;
        }

        if (employee.getPassword() == null || employee.getPassword().isEmpty() || employee.getPassword().toLowerCase().contains("null")) {
            this.error = "PASSWORD darf weder NULL noch leer sein";
            return false;
        }

        if (employee.getEmail() == null || employee.getEmail().isEmpty() || employee.getEmail().toLowerCase().contains("null")) {
            this.error = "EMAIL darf weder NULL noch leer sein";
            return false;
        }

        //E-mail Validation
        if (isValidEmailAddress(employee.getEmail()) == false) {
            this.error = "EMAIL Format ist unkorrekt";
            return false;
        }

        if (this.employeeRepository.existsEmployeeByLogin(employee.getLogin())) {
            this.error = "Es existiert schon ein Employee mit dem Login";
            return false;
        }

        if (this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(employee.getEmail())) {
            this.error = "Es existiert schon ein Employee mit der E-mail";
            return false;
        }

        return true;
    }

    private boolean isValidEmailAddress(String email) {
        return patternMatches(email, EMAIL_REGEX_PATTERN);
    }

    private boolean patternMatches(String email, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    private String getError() {
        return this.error;
    }
}