package com.boivalenko.businessapp.onboarding.web.auth.controller;

import com.boivalenko.businessapp.onboarding.web.auth.obj.JsonExcept;
import com.boivalenko.businessapp.onboarding.web.auth.service.EmployeeDetailsImpl;
import com.boivalenko.businessapp.onboarding.web.auth.service.EmployeeService;
import com.boivalenko.businessapp.onboarding.web.auth.viewmodel.EmployeeVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmployeeService employeeService;

    // Employee Registrierung
    @PutMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody EmployeeVm employeeVm) {
        return this.employeeService.register(employeeVm);
    }

    // Employee Aktivierung (damit er sich einloggen darf
    // und mit der Anwendung weiterarbeiten kann)
    @PostMapping("/activate-account")
    public ResponseEntity<String> activateEmployee(@RequestBody String uuid) {
        return this.employeeService.activateEmployee(uuid);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@Valid @RequestBody EmployeeVm employeeVm) {
        return this.employeeService.logIn(employeeVm);
    }

    //Log Out aus dem System. Es wird Cookie mit 0 Age erstellt,
    // dabei wird alte Cookie überschrieben und im Endeffekt vom Browser entfernt
    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<String> logOut() {
        return this.employeeService.logOut();
    }

    @PostMapping("/update-password")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<String> updatePassword(@RequestBody String password) {
        return this.employeeService.updatePassword(password);
    }

    // Employee Deaktivierung
    @PostMapping("/account-deactivate")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<String> deActivateEmployee(@RequestBody Long employeeId) {
        return this.employeeService.deActivateEmployee(employeeId);
    }

    //resend E-Mail für account aktivation
    @PostMapping("/resend-activate-email")
    public ResponseEntity<String> resendActivateEmail(@RequestBody String usernameOrEmail) {
        return this.employeeService.resendActivateEmail(usernameOrEmail);
    }

    //send E-Mail für password reset, um dann später "update-password" verwenden zu können
    @PostMapping("/send-reset-password-email")
    public ResponseEntity<String> sendResetPasswordEmail(@RequestBody String email) {
        return this.employeeService.sendResetPasswordEmail(email);
    }

    /*
    Wenn sich der Benutzer am Client bereits angemeldet hat, wird diese Methode bei jeder nächsten Anfrage an eine geschlossene Seite aufgerufen.
    Bei dieser Methode senden wir lediglich die Benutzerdaten an den Kunden zurück (Name, E-Mail, Rollen usw.)
    Das Wichtigste ist, dass die Anfrage,
    bevor sie in diese Methode gelangt, den AuthTokenFilter-Filter passiert,
    der sicherstellt, dass das JWT-Cookie gefunden und korrekt ist –
    und den Benutzer automatisch autorisiert.
 */
    @PostMapping("/auto")
    public ResponseEntity<EmployeeVm> autoLogin() { // Body fehlt
        EmployeeDetailsImpl userDetails = (EmployeeDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(userDetails.getEmployee());
    }

    /*
    Einige Exceptions, die bearbeitet werden könnten:
UserAlreadyActivatedException - Employee ist schon aktiviert
UsernameNotFoundException - Login oder E-Mail wurden in DB nicht gefunden
BadCredentialsException - falscher Login oder Password
(es können noch andere Daten von einem Employee sein)

UserOrEmailExistsException - Login oder E-Mail existieren schon
    */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonExcept> handleExceptions(Exception ex) {
        return new ResponseEntity<>(new JsonExcept(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
