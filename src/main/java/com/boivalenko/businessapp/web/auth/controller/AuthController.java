package com.boivalenko.businessapp.web.auth.controller;

import com.boivalenko.businessapp.web.auth.entity.Employee;
import com.boivalenko.businessapp.web.auth.obj.JsonException;
import com.boivalenko.businessapp.web.auth.service.EmployeeService;
import com.boivalenko.businessapp.web.auth.service.UserDetailsImpl;
import com.boivalenko.businessapp.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.web.auth.utils.JwtUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;

    public AuthController(EmployeeService employeeService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, CookieUtils cookieUtils) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cookieUtils = cookieUtils;
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

    @PostMapping("/login")
    public ResponseEntity<Employee> login(@Valid @RequestBody Employee employee) {

        //authentication des Employees
        Authentication authentication = this.authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(employee.getLogin(), employee.getPassword()));

        //add authentication Employees Data in Spring Container
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (userDetails.isActivated() == false) {
            throw new DisabledException("Employee ist nicht aktiviert");
        }

        //Employee ist erfolgreich angemeldet

        //Password braucht man für authentication nur ein mal
        //und dann nicht mehr. Damit Password nirgendwo
        //zufällig auftaucht, kann man den als NULL setzen
        userDetails.getEmployee().setPassword(null);


        String jwt = this.jwtUtils.createAccessToken(userDetails.getEmployee());

        //wird Cookie mit jwt als Value erzeugt
        HttpCookie httpCookie = this.cookieUtils.createJwtCookie(jwt);

        HttpHeaders responseHeaders = new HttpHeaders();
        //wird Cookie in Header hinzugefügt.
        // SET_COOKIE sagt dass es um eine Server Side Cookie geht
        responseHeaders.add(HttpHeaders.SET_COOKIE, httpCookie.toString());

        return ResponseEntity.ok().headers(responseHeaders).body(userDetails.getEmployee());
    }

    //Log Out aus dem System. Es wird Cookie mit 0 Age erstellt,
    // dabei wird alte Cookie überschrieben und Im Endefekt vom Browser entfernt
    @PostMapping("/logout")
    public ResponseEntity logout(){
        HttpCookie cookie = this.cookieUtils.deleteJwtCookie();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(httpHeaders).build();
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
