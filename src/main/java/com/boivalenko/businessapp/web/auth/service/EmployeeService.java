package com.boivalenko.businessapp.web.auth.service;

import com.boivalenko.businessapp.web.auth.entity.Activity;
import com.boivalenko.businessapp.web.auth.entity.Employee;
import com.boivalenko.businessapp.web.auth.repository.ActivityRepository;
import com.boivalenko.businessapp.web.auth.repository.EmployeeRepository;
import com.boivalenko.businessapp.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.web.auth.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final ActivityRepository activityRepository;

    // Regex laut RFC822
    private static final String EMAIL_REGEX_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private String error;

    public ResponseEntity<Employee> register(Employee employee) {

        if (isEmployeeValid(employee) == false) {
            return new ResponseEntity(getError(), HttpStatus.NOT_ACCEPTABLE);
        }

        //Einweg-Hash-Passwort-Encoder : Bcrypt
        employee.setPassword(this.passwordEncoder.encode(employee.getPassword()));

        this.employeeRepository.save(employee);

        return ResponseEntity.ok().build();
    }

    public Activity findActivityByUuid(String uuid){
        return this.activityRepository.findActivityByUuid(uuid).get();
    }

    public ResponseEntity activateEmployee(String uuid){

        // UUID Prüfung
        Activity activity = this.findActivityByUuid(uuid);
        if (activity == null) {
            return new ResponseEntity("Activity nicht gefunden. UUID:" + uuid, HttpStatus.NOT_ACCEPTABLE);
        }

        //wenn der Employee bereits zuvor aktiviert wurde
        if (activity.getActivated() == true) {
            return new ResponseEntity("Employee ist schon aktiviert", HttpStatus.NOT_ACCEPTABLE);
        }

        // gibt die Anzahl der aktualisierten Datensätze zurück (sollte 1 sein)
        int updatedCount = this.activityRepository.activate(uuid);
        if(updatedCount != 1) {
            return new ResponseEntity("Aktivierung des Employee ist nicht geklappt", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity("Employee ist erfolgreich aktiviert", HttpStatus.OK);
    }


    public ResponseEntity deActivateEmployee(String uuid){

        // UUID Prüfung
        Activity activity = this.findActivityByUuid(uuid);
        if (activity == null) {
            return new ResponseEntity("Activity nicht gefunden. UUID:" + uuid, HttpStatus.NOT_ACCEPTABLE);
        }

        //wenn der Employee bereits zuvor deaktiviert wurde
        if (activity.getActivated() == false) {
            return new ResponseEntity("Employee ist schon deaktiviert", HttpStatus.NOT_ACCEPTABLE);
        }

        // gibt die Anzahl der aktualisierten Datensätze zurück (sollte 1 sein)
        int updatedCount = this.activityRepository.deActivate(uuid);
        if(updatedCount != 1) {
            return new ResponseEntity("Deaktivierung des Employee ist nicht geklappt", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity("Employee ist erfolgreich deaktiviert", HttpStatus.OK);
    }

    public int updatePassword(String password, String username) {
        return this.employeeRepository.upddatePassword(password, username);
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

    public ResponseEntity<Employee> login(Employee employee) {
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

    public ResponseEntity logout() {
        HttpCookie cookie = this.cookieUtils.deleteJwtCookie();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(httpHeaders).build();
    }

    public ResponseEntity<Boolean> updatePassword(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        int updateCount = updatePassword(this.passwordEncoder.encode(password), user.getUsername());
        return ResponseEntity.ok(updateCount == 1);
    }
}