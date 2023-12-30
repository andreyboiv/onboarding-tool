package com.boivalenko.businessapp.teamtasksplanning.web.auth.controller;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.obj.JsonException;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmployeeService employeeService;

    @PutMapping("/register")
    public ResponseEntity register(@Valid @RequestBody Employee employee) {
        return this.employeeService.register(employee);
    }

    // Employee Aktivierung (damit er sich anmelden
    // und mit der Anwendung weiterarbeiten kann)
    @PostMapping("/activate-account")
    public ResponseEntity activateEmployee(@RequestBody String uuid) {
        return this.employeeService.activateEmployee(uuid);
    }

    @PostMapping("/login")
    public ResponseEntity logIn(@Valid @RequestBody Employee employee) {
        return this.employeeService.logIn(employee);
    }

    //Log Out aus dem System. Es wird Cookie mit 0 Age erstellt,
    // dabei wird alte Cookie überschrieben und Im Endefekt vom Browser entfernt
    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity logOut() {
        return this.employeeService.logOut();
    }

    @PostMapping("/update-password")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity updatePassword(@RequestBody String password) {
        return this.employeeService.updatePassword(password);
    }

    // Employee Deaktivierung
    @PostMapping("/deactivate-account")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deActivateEmployee(@RequestBody String uuid) {
        return this.employeeService.deActivateEmployee(uuid);
    }

    //resend E-Mail für account aktivation
    @PostMapping("/resend-activate-email")
    public ResponseEntity resendActivateEmail(@RequestBody String usernameOrEmail) {
        return this.employeeService.resendActivateEmail(usernameOrEmail);
    }

    //send E-Mail für password reset, um dann später "update-password" verwenden zu können
    @PostMapping("/send-reset-password-email")
    public ResponseEntity sendResetPasswordEmail(@RequestBody String email) {
        return this.employeeService.sendResetPasswordEmail(email);
    }

    /*
    Einige Exceptions, die bearbeitet werden könnten:
UserAlreadyActivatedException - Employee ist schon aktiviert
UsernameNotFoundException - Login oder Email wurden in DB nicht gefunden
BadCredentialsException - falscher Login oder Password
(es können noch andere Daten von einem Employee sein)

UserOrEmailExistsException - Login oder Email existieren schon
    */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handleExceptions(Exception ex) {
        return new ResponseEntity(new JsonException(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
