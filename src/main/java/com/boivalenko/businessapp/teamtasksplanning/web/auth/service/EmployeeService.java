package com.boivalenko.businessapp.teamtasksplanning.web.auth.service;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Activity;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.repository.ActivityRepository;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.repository.EmployeeRepository;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.EmployeeValid;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.JwtUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel.EmployeeVm;
import jakarta.transaction.Transactional;
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

import java.util.Optional;

import static com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.EmployeeValid.MIN_PASSWORD_LENGTH;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmailService emailService;
    private final EmployeeDetailsServiceImpl employeeDetailsService;

    private final EmployeeRepository employeeRepository;
    private final ActivityRepository activityRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> register(EmployeeVm employeeVm) {

        String employeeValid = EmployeeValid.isEmployeeValid(employeeVm);
        if (!employeeValid.isEmpty()) {
            return new ResponseEntity<>(employeeValid, HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.employeeRepository.existsEmployeeByLogin(employeeVm.getLogin())) {
            String error = "Es existiert schon ein Employee mit dem Login";
            return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(employeeVm.getEmail())) {
            String error = "Es existiert schon ein Employee mit der E-mail";
            return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
        }

        //Einweg-Hash-Passwort-Encoder: Bcrypt
        employeeVm.setPassword(this.passwordEncoder.encode(employeeVm.getPassword()));

        Employee employee = this.getEmployeeFromEmployeePojo(employeeVm);
        this.employeeRepository.save(employee);

        Activity activity = null;

        Optional<Activity> activityById = this.activityRepository.findActivityById(employee.getId());

        if (activityById.isPresent()) {
            activity = activityById.get();
        }

        if (activity == null) {
            return new ResponseEntity<>("Activity nicht gefunden. EmployeeID:" + employee.getId(), HttpStatus.NOT_ACCEPTABLE);
        }

        //es wird eine E-Mail mit Account Aktivierung Benachrichtigung herausgeschickt
        this.emailService.sendActivationEmail(employeeVm.getEmail(), employeeVm.getLogin(), activity.getUuid());

        return new ResponseEntity<>("Employee ist erfolgreich registriert. Die E-mail mit einem Aktivierungslink ist abgeschickt", HttpStatus.OK);
    }

    private Employee getEmployeeFromEmployeePojo(EmployeeVm employeeVm) {
        Employee employee = new Employee();

        employee.setId(employeeVm.getId());
        employee.setLogin(employeeVm.getLogin());
        employee.setPassword(employeeVm.getPassword());
        employee.setEmail(employeeVm.getEmail());
        employee.setPowers(employeeVm.getPowers());
        employee.setActivity(employeeVm.getActivity());

        return employee;
    }

    public ResponseEntity<String> activateEmployee(String uuid){

        if (uuid == null || uuid.isEmpty()) {
            return new ResponseEntity("UUID darf nicht leer sein", HttpStatus.NOT_ACCEPTABLE);
        }

        // UUID Prüfung
        Activity activity = null;

        Optional<Activity> activityById = this.activityRepository.findActivityByUuid(uuid);

        if (activityById.isPresent()) {
            activity = activityById.get();
        }

        if (activity == null) {
            return new ResponseEntity<>("Activity nicht gefunden. UUID:" + uuid, HttpStatus.NOT_ACCEPTABLE);
        }

        //wenn der Employee bereits zuvor aktiviert wurde
        if (Boolean.TRUE.equals(activity.getActivated())) {
            return new ResponseEntity<>("Employee ist schon aktiviert", HttpStatus.NOT_ACCEPTABLE);
        }

        // gibt die Anzahl der aktualisierten Datensätze zurück (sollte 1 sein)
        int updatedCount = this.activityRepository.activate(uuid);

        if (updatedCount != 1) {
            return new ResponseEntity<>("Aktivierung des Employee ist nicht geklappt", HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Employee> employee = this.employeeRepository.findEmployeeByActivity_Uuid(uuid);

        if (employee.isPresent()) {
            //es wird eine E-Mail mit weiteren Hinweisen nach der erfolgreichen Aktivierung abgeschickt.
            this.emailService.sendAktivierungsEmail(employee.get().getEmail(), employee.get().getLogin());
        }

        return new ResponseEntity<>("Employee ist erfolgreich aktiviert. Sie erhalten bald eine E-mail mit Begrüßung", HttpStatus.OK);
    }

    public ResponseEntity<String> deActivateEmployee(String uuid) {

        if (uuid == null || uuid.isEmpty()) {
            return new ResponseEntity("UUID darf nicht leer sein", HttpStatus.NOT_ACCEPTABLE);
        }

        // UUID Prüfung
        Activity activity = null;

        Optional<Activity> activityById = this.activityRepository.findActivityByUuid(uuid);

        if (activityById.isPresent()) {
            activity = activityById.get();
        }

        if (activity == null) {
            return new ResponseEntity<>("Activity nicht gefunden. UUID:" + uuid, HttpStatus.NOT_ACCEPTABLE);
        }

        // Wenn der Employee bereits zuvor deaktiviert wurde
        if (Boolean.FALSE.equals(activity.getActivated())) {
            return new ResponseEntity<>("Employee ist schon deaktiviert", HttpStatus.NOT_ACCEPTABLE);
        }

        // Gibt die Anzahl der aktualisierten Datensätze zurück (sollte 1 sein)
        int updatedCount = this.activityRepository.deActivate(uuid);

        if (updatedCount != 1) {
            return new ResponseEntity<>("Deaktivierung des Employee ist nicht geklappt", HttpStatus.NOT_ACCEPTABLE);
        }

        //es wird eine E-Mail mit weiteren Hinweisen nach der erfolgreichen Deaktivierung abgeschickt.
        this.emailService.sendDeaktivierungsEmail(activity.getEmployeeToActivity().getEmail(), activity.getEmployeeToActivity().getLogin());

        return new ResponseEntity<>("Der Employee (uuid:" + uuid + ") ist erfolgreich deaktiviert. Dabei erhält er eine Benachrichtigung per E-mail", HttpStatus.OK);
    }

    public ResponseEntity<String> updatePassword(String password) {

        if (password == null || password.isEmpty()) {
            return new ResponseEntity<>("Password darf nicht leer sein", HttpStatus.NOT_ACCEPTABLE);
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return new ResponseEntity<>("PASSWORD muss mindestens " + MIN_PASSWORD_LENGTH + " Symbole enthalten", HttpStatus.NOT_ACCEPTABLE);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) authentication.getPrincipal();

        int updateCount = this.employeeRepository.updatePassword(employeeDetails.getUsername(), this.passwordEncoder.encode(password));

        if (updateCount == 1) {
            this.emailService.sendPasswordGeandertEmail(employeeDetails.getEmail(), employeeDetails.getUsername());
            return new ResponseEntity<>("Password wurde erfolgreich geändert", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Password wurde nicht geändert", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity<String> logIn(EmployeeVm employeeVm) {

        String employeeValid = EmployeeValid.isEmployeeValidWithOutEmail(employeeVm);
        if (!employeeValid.equals("")) {
            return new ResponseEntity<>(employeeValid, HttpStatus.NOT_ACCEPTABLE);
        }

        //authentication des Employees (es wird nachgeprüft, ob Login und Password korrekt sind)
        Authentication authentication = this.authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(employeeVm.getLogin(), employeeVm.getPassword()));

        //add authentication Employees Data in Spring Container
        SecurityContextHolder.getContext().setAuthentication(authentication);

        EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) authentication.getPrincipal();

        if (Boolean.FALSE.equals(employeeDetails.isActivated())) {
            throw new DisabledException("Employee ist nicht aktiviert");
        }

        //Employee hat sich erfolgreich eingeloggt

        String jwt = this.jwtUtils.createAccessToken(employeeDetails.getEmployee());

        //wird Cookie mit jwt als Value erzeugt
        HttpCookie httpCookie = this.cookieUtils.createJwtCookie(jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        //wird Cookie in Header hinzugefügt.
        // SET_COOKIE sagt, dass es um eine Server Side Cookie geht
        httpHeaders.add(HttpHeaders.SET_COOKIE, httpCookie.toString());

        return new ResponseEntity<>("Employee hat sich erfolgreich eingeloggt", httpHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> logOut() {
        HttpCookie cookie = this.cookieUtils.deleteJwtCookie();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>("Employee hat sich erfolgreich ausgeloggt", httpHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> resendActivateEmail(String usernameOrEmail) {
        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
            return new ResponseEntity<>("Weder Login noch E-mail dürfen leer sein", HttpStatus.NOT_ACCEPTABLE);
        }

        EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) employeeDetailsService.loadUserByUsername(usernameOrEmail);
        EmployeeVm employee = employeeDetails.getEmployee();

        if (Boolean.TRUE.equals(employee.getActivity().getActivated())) {
            return new ResponseEntity<>("Employee ist schon aktiviert. Sie brauchen keine Reaktivierung", HttpStatus.NOT_ACCEPTABLE);
        }

        this.emailService.sendActivationEmail(employee.getEmail(), employee.getLogin(), employee.getActivity().getUuid());

        return new ResponseEntity<>("Die E-mail mit dem Aktivierungslink " +
                "erhalten Sie bald wieder. Schauen Sie bitte außerdem Ihren Spam-Ordner an", HttpStatus.OK);
    }

    public ResponseEntity<String> sendResetPasswordEmail(String userDetails) {
        if (userDetails == null || userDetails.isEmpty()) {
            return new ResponseEntity<>("E-mail darf nicht leer sein", HttpStatus.NOT_ACCEPTABLE);
        }

        EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) employeeDetailsService.loadUserByUsername(userDetails);
        EmployeeVm employee = employeeDetails.getEmployee();

        emailService.sendResetPassword(employee.getEmail(), jwtUtils.createEmailResetToken(employee));

        return new ResponseEntity<>("Die E-mail mit dem entsprenden Link (mit Token Bearer), um das Password für Ihr Account zu ändern," +
                " erhalten Sie bald wieder. Schauen Sie bitte außerdem Ihren Spam-Ordner an", HttpStatus.OK);
    }
}
