package com.boivalenko.businessapp.web.auth.controller;

import com.boivalenko.businessapp.web.auth.entity.Employee;
import com.boivalenko.businessapp.web.auth.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(EmployeeService employeeService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/register")
    public ResponseEntity<Employee> register(@Valid @RequestBody Employee employee) {
        return this.employeeService.register(employee, passwordEncoder);
    }

    // Employee Aktivierung (damit er sich anmelden
    // und mit der Anwendung weiterarbeiten kann)
    @PostMapping("/activate-account")
    public ResponseEntity<Boolean> activateEmployee(@RequestBody String uuid) {
        return this.employeeService.activateEmployee(uuid);
    }

    // Employee Deaktivierung
    @PostMapping("/deactivate-account")
    public ResponseEntity<Boolean> deActivateEmployee(@RequestBody String uuid) {
        return this.employeeService.deActivateEmployee(uuid);
    }

}
