package com.boivalenko.businessapp.onboarding.web.auth.service;

import com.boivalenko.businessapp.onboarding.web.auth.entity.Activity;
import com.boivalenko.businessapp.onboarding.web.auth.entity.Employee;
import com.boivalenko.businessapp.onboarding.web.auth.repository.ActivityRepository;
import com.boivalenko.businessapp.onboarding.web.auth.repository.EmployeeRepository;
import com.boivalenko.businessapp.onboarding.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.onboarding.web.auth.utils.EmployeeValid;
import com.boivalenko.businessapp.onboarding.web.auth.utils.JwtUtils;
import com.boivalenko.businessapp.onboarding.web.auth.viewmodel.EmployeeVm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.boivalenko.businessapp.onboarding.web.auth.utils.EmployeeValid.MIN_PASSWORD_LENGTH;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    public static final String ES_EXISTIERT_SCHON_EIN_EMPLOYEE_MIT_DEM_LOGIN = "Es existiert schon ein Employee mit dem Login";
    public static final String ES_EXISTIERT_SCHON_EIN_EMPLOYEE_MIT_DER_E_MAIL = "Es existiert schon ein Employee mit der E-mail";
    public static final String EMPLOYEE_IST_ERFOLGREICH_REGISTRIERT_DIE_E_MAIL_MIT_EINEM_AKTIVIERUNGSLINK_IST_ABGESCHICKT = "Danke für die Registrierung. Schau bitte dein Postfach (vielleicht auch den Spam-Ordner) in ein paar Minuten";
    public static final String UUID_DARF_NICHT_LEER_SEIN = "UUID darf nicht leer sein";
    public static final String ACTIVITY_NICHT_GEFUNDEN_UUID = "Activity nicht gefunden. UUID:";
    public static final String EMPLOYEE_IST_SCHON_AKTIVIERT = "Employee ist schon aktiviert";
    public static final String AKTIVIERUNG_DES_EMPLOYEE_IST_NICHT_GEKLAPPT = "Aktivierung des Employee ist nicht geklappt";
    public static final String EMPLOYEE_NICHT_GEFUNDEN_UUID = "Employee nicht gefunden. UUID:";
    public static final String ES_IST_EIN_FEHLER_WAEHREND_AKTIVATION_AUFGETRETEN_PROBIEREN_SIE_EINE_AKTIVIERUNGS_E_MAIL_NOCH_MAL_GENERIEREN_ZU_LASSEN = "Es ist ein Fehler während Aktivation aufgetreten. Probieren Sie, eine aktivierungs E-mail noch mal generieren zu lassen";
    public static final String EMPLOYEE_IST_ERFOLGREICH_AKTIVIERT_SIE_ERHALTEN_BALD_EINE_E_MAIL_MIT_BEGR_UESS_UNG = "Employee ist erfolgreich aktiviert. Sie erhalten bald eine E-mail mit Begrüßung";
    public static final String EMPLOYEE_IST_SCHON_DEAKTIVIERT = "Employee ist schon deaktiviert";
    public static final String DEAKTIVIERUNG_DES_EMPLOYEE_IST_NICHT_GEKLAPPT = "Deaktivierung des Employee ist nicht geklappt";
    public static final String DER_EMPLOYEE_UUID = "Der Employee (uuid:";
    public static final String IST_ERFOLGREICH_DEAKTIVIERT_DABEI_ERHAELT_ER_EINE_BENACHRICHTIGUNG_PER_E_MAIL = ") ist erfolgreich deaktiviert. Dabei erhält er eine Benachrichtigung per E-mail";
    public static final String PASSWORD_DARF_NICHT_LEER_SEIN = "Passwort darf nicht leer sein";
    public static final String PASSWORD_MUSS_MINDESTENS = "Passwort muss mindestens ";
    public static final String SYMBOLE_ENTHALTEN = " Symbole enthalten";
    public static final String PASSWORD_WURDE_NICHT_GEAENDERT = "Passwort wurde nicht geändert";
    public static final String PASSWORD_WURDE_ERFOLGREICH_GEAENDERT = "Passwort wurde erfolgreich geändert";
    public static final String EMPLOYEE_HAT_SICH_ERFOLGREICH_AUSGELOGGT = "Employee hat sich erfolgreich ausgeloggt";
    public static final String WEDER_LOGIN_NOCH_E_MAIL_DUERFEN_LEER_SEIN = "Weder Login noch E-mail dürfen leer sein";
    public static final String EMPLOYEE_IST_SCHON_AKTIVIERT_SIE_BRAUCHEN_KEINE_REAKTIVIERUNG = "Dein Account ist schon aktiviert. Du brauchst keine Reaktivierung...";
    public static final String EMAIL_MIT_AKTIVIERUNGSLINK = "Die E-mail mit dem Aktivierungslink " +
            "erhalten Sie bald wieder. Schauen Sie bitte außerdem Ihren Spam-Ordner an";
    public static final String E_MAIL_DARF_NICHT_LEER_SEIN = "E-mail darf nicht leer sein";
    public static final String RESET_PASSWORD_EMAIL = "Schau bitte dein Postfach (vielleicht auch den Spam-Ordner) in ein paar Minuten. Da erhälst du weitere Anweisungen, um dein Passwort zurücksetzen zu können";
    public static final String EMPLOYEE_IST_NICHT_AKTIVIERT = "Employee ist nicht aktiviert";

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

        if (this.employeeRepository.existsEmployeeByLoginEqualsIgnoreCase(employeeVm.getLogin())) {
            String error = ES_EXISTIERT_SCHON_EIN_EMPLOYEE_MIT_DEM_LOGIN + " \"" + employeeVm.getLogin() +"\"";
            return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(employeeVm.getEmail())) {
            String error = ES_EXISTIERT_SCHON_EIN_EMPLOYEE_MIT_DER_E_MAIL + " \"" + employeeVm.getEmail() +"\"";
            return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
        }

        //Einweg-Hash-Passwort-Encoder: Bcrypt
        employeeVm.setPassword(this.passwordEncoder.encode(employeeVm.getPassword()));

        Employee employee = this.getEmployeeFromEmployeePojo(employeeVm);
        this.employeeRepository.save(employee);

        Activity activity = new Activity();
        activity.setEmployeeToActivity(employee);
        activity.setUuid(UUID.randomUUID().toString());
        this.activityRepository.save(activity);

        //es wird eine E-Mail mit Account Aktivierung Benachrichtigung herausgeschickt
        this.emailService.sendActivationEmail(employeeVm.getEmail(), employeeVm.getLogin(), activity.getUuid());

        return new ResponseEntity<>(EMPLOYEE_IST_ERFOLGREICH_REGISTRIERT_DIE_E_MAIL_MIT_EINEM_AKTIVIERUNGSLINK_IST_ABGESCHICKT, HttpStatus.OK);
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
            return new ResponseEntity<>(UUID_DARF_NICHT_LEER_SEIN, HttpStatus.NOT_ACCEPTABLE);
        }

        // UUID Prüfung
        Activity activity = null;

        Optional<Activity> activityById = this.activityRepository.findActivityByUuid(uuid);

        if (activityById.isPresent()) {
            activity = activityById.get();
        }

        if (activity == null) {
            return new ResponseEntity<>(ES_IST_EIN_FEHLER_WAEHREND_AKTIVATION_AUFGETRETEN_PROBIEREN_SIE_EINE_AKTIVIERUNGS_E_MAIL_NOCH_MAL_GENERIEREN_ZU_LASSEN, HttpStatus.NOT_ACCEPTABLE);
        }

        //wenn der Employee bereits zuvor aktiviert wurde
        if (Boolean.TRUE.equals(activity.getActivated())) {
            return new ResponseEntity<>(EMPLOYEE_IST_SCHON_AKTIVIERT, HttpStatus.NOT_ACCEPTABLE);
        }

        // gibt die Anzahl der aktualisierten Datensätze zurück (sollte 1 sein)
        int updatedCount = this.activityRepository.activate(uuid);

        if (updatedCount != 1) {
            return new ResponseEntity<>(AKTIVIERUNG_DES_EMPLOYEE_IST_NICHT_GEKLAPPT, HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Employee> employee = this.employeeRepository.findEmployeeByActivity_Uuid(uuid);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(EMPLOYEE_NICHT_GEFUNDEN_UUID + uuid, HttpStatus.NOT_ACCEPTABLE);
        }

        //es wird eine E-Mail mit weiteren Hinweisen nach der erfolgreichen Aktivierung abgeschickt.
        this.emailService.sendAktivierungsEmail(employee.get().getEmail(), employee.get().getLogin());

        return new ResponseEntity<>(EMPLOYEE_IST_ERFOLGREICH_AKTIVIERT_SIE_ERHALTEN_BALD_EINE_E_MAIL_MIT_BEGR_UESS_UNG, HttpStatus.OK);
    }

    public ResponseEntity<String> deActivateEmployee(Long employeeID) {

        if (employeeID == null || employeeID.equals(0L)) {
            return new ResponseEntity<>(UUID_DARF_NICHT_LEER_SEIN, HttpStatus.NOT_ACCEPTABLE);
        }

        Activity activity = null;

        Optional<Activity> activityById = this.activityRepository.findActivityByEmployeeToActivity_Id(employeeID);

        if (activityById.isPresent()) {
            activity = activityById.get();
        }

        if (activity == null) {
            return new ResponseEntity<>(ACTIVITY_NICHT_GEFUNDEN_UUID + employeeID, HttpStatus.NOT_ACCEPTABLE);
        }

        // Wenn der Employee bereits zuvor deaktiviert wurde
        if (Boolean.FALSE.equals(activity.getActivated())) {
            return new ResponseEntity<>(EMPLOYEE_IST_SCHON_DEAKTIVIERT, HttpStatus.NOT_ACCEPTABLE);
        }

        // Gibt die Anzahl der aktualisierten Datensätze zurück (sollte 1 sein)
        int updatedCount = this.activityRepository.deActivate(employeeID);

        if (updatedCount != 1) {
            return new ResponseEntity<>(DEAKTIVIERUNG_DES_EMPLOYEE_IST_NICHT_GEKLAPPT, HttpStatus.NOT_ACCEPTABLE);
        }

        //es wird eine E-Mail mit weiteren Hinweisen nach der erfolgreichen Deaktivierung abgeschickt.
        this.emailService.sendDeaktivierungsEmail(activity.getEmployeeToActivity().getEmail(), activity.getEmployeeToActivity().getLogin());

        //es wird eine E-Mail mit weiteren Hinweisen nach der erfolgreichen Deaktivierung abgeschickt.
        this.emailService.sendDeaktivierungsEmailtoAdmin(activity.getEmployeeToActivity().getEmail(), activity.getEmployeeToActivity().getLogin());

        return new ResponseEntity<>(DER_EMPLOYEE_UUID + employeeID + IST_ERFOLGREICH_DEAKTIVIERT_DABEI_ERHAELT_ER_EINE_BENACHRICHTIGUNG_PER_E_MAIL, HttpStatus.OK);
    }

    public ResponseEntity<String> updatePassword(String password) {

        if (password == null || password.isEmpty()) {
            return new ResponseEntity<>(PASSWORD_DARF_NICHT_LEER_SEIN, HttpStatus.NOT_ACCEPTABLE);
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return new ResponseEntity<>(PASSWORD_MUSS_MINDESTENS + MIN_PASSWORD_LENGTH + SYMBOLE_ENTHALTEN, HttpStatus.NOT_ACCEPTABLE);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) authentication.getPrincipal();

        int updateCount = this.employeeRepository.updatePassword(employeeDetails.getUsername(), this.passwordEncoder.encode(password));

        if (updateCount != 1) {
            return new ResponseEntity<>(PASSWORD_WURDE_NICHT_GEAENDERT, HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByLogin(employeeDetails.getUsername());

        if (employeeByLogin.isPresent()) {
            Employee employee = employeeByLogin.get();
            this.emailService.sendPasswordGeandertEmail(employee.getEmail(), employeeDetails.getUsername());
        }

        return new ResponseEntity<>(PASSWORD_WURDE_ERFOLGREICH_GEAENDERT, HttpStatus.OK);
    }

    public ResponseEntity<String> logIn(EmployeeVm employeeVm) {

        String employeeValid = EmployeeValid.isValidLoginAndPassword(employeeVm);
        if (!employeeValid.isEmpty()) {
            return new ResponseEntity<>(employeeValid, HttpStatus.NOT_ACCEPTABLE);
        }

        //authentication des Employees (es wird nachgeprüft, ob Login und Password korrekt sind)
        Authentication authentication = this.authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(employeeVm.getLogin(), employeeVm.getPassword()));

        //add authentication Employees Data in Spring Container
        SecurityContextHolder.getContext().setAuthentication(authentication);

        EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) authentication.getPrincipal();

        if (Boolean.FALSE.equals(employeeDetails.isActivated())) {
            return new ResponseEntity<>(EMPLOYEE_IST_NICHT_AKTIVIERT, HttpStatus.NOT_ACCEPTABLE);
        }

        //Employee hat sich erfolgreich eingeloggt

        String jwt = this.jwtUtils.createAccessToken(employeeDetails.getEmployee());

        //wird Cookie mit jwt als Value erzeugt
        HttpCookie httpCookie = this.cookieUtils.createJwtCookie(jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        //wird Cookie in Header hinzugefügt.
        // SET_COOKIE sagt, dass es um eine Server Side Cookie geht
        httpHeaders.add(HttpHeaders.SET_COOKIE, httpCookie.toString());

        return new ResponseEntity<>(employeeDetails.getID().toString(), httpHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> logOut() {
        HttpCookie cookie = this.cookieUtils.deleteJwtCookie();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(EMPLOYEE_HAT_SICH_ERFOLGREICH_AUSGELOGGT, httpHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> resendActivateEmail(String usernameOrEmail) {
        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
            return new ResponseEntity<>(WEDER_LOGIN_NOCH_E_MAIL_DUERFEN_LEER_SEIN, HttpStatus.NOT_ACCEPTABLE);
        }

        EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) employeeDetailsService.loadUserByUsername(usernameOrEmail);
        EmployeeVm employee = employeeDetails.getEmployee();

        if (Boolean.TRUE.equals(employee.getActivity().getActivated())) {
            return new ResponseEntity<>(EMPLOYEE_IST_SCHON_AKTIVIERT_SIE_BRAUCHEN_KEINE_REAKTIVIERUNG, HttpStatus.NOT_ACCEPTABLE);
        }

        this.emailService.sendActivationEmail(employee.getEmail(), employee.getLogin(), employee.getActivity().getUuid());

        return new ResponseEntity<>(EMAIL_MIT_AKTIVIERUNGSLINK, HttpStatus.OK);
    }

    public ResponseEntity<String> sendResetPasswordEmail(String email) {
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>(E_MAIL_DARF_NICHT_LEER_SEIN, HttpStatus.NOT_ACCEPTABLE);
        }

        EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) employeeDetailsService.loadUserByUsername(email);
        EmployeeVm employee = employeeDetails.getEmployee();

        emailService.sendResetPassword(employee.getEmail(), jwtUtils.createEmailResetToken(employee));

        return new ResponseEntity<>(RESET_PASSWORD_EMAIL, HttpStatus.OK);
    }
}
