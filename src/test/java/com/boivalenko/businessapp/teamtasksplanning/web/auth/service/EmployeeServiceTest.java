package com.boivalenko.businessapp.teamtasksplanning.web.auth.service;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Activity;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.repository.ActivityRepository;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.repository.EmployeeRepository;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.EmployeeValid;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.JwtUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel.EmployeeVm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private EmailService emailService;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private CookieUtils cookieUtils;

    @Mock
    private EmployeeDetailsServiceImpl employeeDetailsService;

    // register
    // Negative Tests
    @Test
    void register_employeeValid_employee_not_null() {
        EmployeeVm employeeVm = null;

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.EMPLOYEE_DARF_NICHT_NULL_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_id_not_empty() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setId(1L);
        employeeVm.setLogin("musterLogin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.ID_WIRD_AUTOMATISCH_GENERIERT_MAN_MUSS_DA_NICHTS_EINGEBEN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_login_equals_null() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin(null);
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.LOGIN_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_login_equals_leerString() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.LOGIN_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_login_equals_stringContainsNull() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("StringNuLLString");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.LOGIN_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_loginLength_kleiner_min_login_length() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("Strin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.LOGIN_MUSS_MINDESTENS +
                EmployeeValid.MIN_LOGIN_LENGTH +
                EmployeeValid.SYMBOLE_ENTHALTEN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_password_equals_null() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword(null);
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.PASSWORD_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_password_equals_leer_string() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.PASSWORD_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_password_contains_null() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("StringNuLLString");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.PASSWORD_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_password_minlaenge() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stri");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.PASSWORD_MUSS_MINDESTENS + EmployeeValid.MIN_PASSWORD_LENGTH + EmployeeValid.SYMBOLE_ENTHALTEN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_email_null() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stringds");
        employeeVm.setEmail(null);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.EMAIL_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_email_empty() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stringds");
        employeeVm.setEmail("");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.EMAIL_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_email_contains_null1() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stringds");
        employeeVm.setEmail("musteremailnullmus@muster.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.EMAIL_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_email_contains_null2() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stringds");
        employeeVm.setEmail("musteremailmus@musternull.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.EMAIL_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_email_not_valid1() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stringds");
        employeeVm.setEmail("musteremailmusmuster.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.EMAIL_FORMAT_IST_UNKORREKT, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_email_not_valid2() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stringds");
        employeeVm.setEmail("musteremailmus@muster@sdf.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.EMAIL_FORMAT_IST_UNKORREKT, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employeeValid_employee_email_not_valid3() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stringds");
        employeeVm.setEmail("musteremailmus@mustersdf.de@");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);
        assertEquals(EmployeeValid.EMAIL_FORMAT_IST_UNKORREKT, employeeResponseEntity.getBody());

        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employee_not_exists_by_login() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("StringLogin");
        employeeVm.setPassword("StringPassword");
        employeeVm.setEmail("muster@muster.de");

        when(this.employeeRepository.existsEmployeeByLogin(employeeVm.getLogin())).thenReturn(true);
        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);

        assertEquals(EmployeeService.ES_EXISTIERT_SCHON_EIN_EMPLOYEE_MIT_DEM_LOGIN, employeeResponseEntity.getBody());
        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void register_employee_not_exists_by_email() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("StringLogin");
        employeeVm.setPassword("StringPassword");
        employeeVm.setEmail("muster@muster.de");

        when(this.employeeRepository.existsEmployeeByLogin(employeeVm.getLogin())).thenReturn(false);
        when(this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(employeeVm.getEmail())).thenReturn(true);
        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);

        assertEquals(EmployeeService.ES_EXISTIERT_SCHON_EIN_EMPLOYEE_MIT_DER_E_MAIL, employeeResponseEntity.getBody());
        verify(this.employeeRepository, times(0)).save(any(Employee.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // Register
    // Positive Tests
    @Test
    void register() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("StringLogin");
        employeeVm.setPassword("StringPassword");
        employeeVm.setEmail("muster@muster.de");

        Activity activity = new Activity("uuid", null, new Employee());

        when(this.employeeRepository.existsEmployeeByLogin(employeeVm.getLogin())).thenReturn(false);
        when(this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(employeeVm.getEmail())).thenReturn(false);

        Optional<Activity> activityById = Optional.of(activity);

        when(this.activityRepository.findActivityById(employeeVm.getId())).thenReturn(activityById);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.register(employeeVm);

        verify(this.employeeRepository, times(1)).save(any(Employee.class));

        assertEquals(EmployeeService.EMPLOYEE_IST_ERFOLGREICH_REGISTRIERT_DIE_E_MAIL_MIT_EINEM_AKTIVIERUNGSLINK_IST_ABGESCHICKT, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // activateEmployee
    // Negative Tests
    @Test
    void activateEmployee_uuid_null() {

        ResponseEntity<String> employeeResponseEntity = this.employeeService.activateEmployee(null);

        verify(this.activityRepository, times(0)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.UUID_DARF_NICHT_LEER_SEIN, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void activateEmployee_uuid_is_empty() {

        ResponseEntity<String> employeeResponseEntity = this.employeeService.activateEmployee("");

        verify(this.activityRepository, times(0)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.UUID_DARF_NICHT_LEER_SEIN, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void activateEmployee_activity_nicht_gefunden() {

        String someUUID = "SomeUUID";

        Optional<Activity> optional = Optional.empty();
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optional);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.activateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.ACTIVITY_NICHT_GEFUNDEN_UUID + someUUID, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void activateEmployee_employee_aktiviert() {

        String someUUID = "SomeUUID";

        boolean employeeAktiviert = true;

        Optional<Activity> optional = Optional.of(new Activity(someUUID, employeeAktiviert, null));
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optional);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.activateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.EMPLOYEE_IST_SCHON_AKTIVIERT, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void activateEmployee_employee_aktivierung_nicht_geklappt() {

        String someUUID = "SomeUUID";

        boolean employeeAktiviert = false;

        Optional<Activity> optional = Optional.of(new Activity(someUUID, employeeAktiviert, null));
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optional);
        when(this.activityRepository.activate(someUUID)).thenReturn(0);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.activateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.AKTIVIERUNG_DES_EMPLOYEE_IST_NICHT_GEKLAPPT, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void activateEmployee_employee_nicht_gefunden() {

        String someUUID = "SomeUUID";

        boolean employeeAktiviert = false;

        Optional<Activity> optional = Optional.of(new Activity(someUUID, employeeAktiviert, null));
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optional);
        when(this.activityRepository.activate(someUUID)).thenReturn(1);
        Optional<Employee> optionalEmployee = Optional.empty();
        when(this.employeeRepository.findEmployeeByActivity_Uuid(someUUID)).thenReturn(optionalEmployee);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.activateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.EMPLOYEE_NICHT_GEFUNDEN_UUID + someUUID, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // activateEmployee
    // Positive Tests

    @Test
    void activateEmployee() {
        String someUUID = "SomeUUID";

        boolean employeeAktiviert = false;

        Optional<Activity> optional = Optional.of(new Activity(someUUID, employeeAktiviert, null));
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optional);
        when(this.activityRepository.activate(someUUID)).thenReturn(1);
        Optional<Employee> optionalEmployee = Optional.of(new Employee());
        when(this.employeeRepository.findEmployeeByActivity_Uuid(someUUID)).thenReturn(optionalEmployee);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.activateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.EMPLOYEE_IST_ERFOLGREICH_AKTIVIERT_SIE_ERHALTEN_BALD_EINE_E_MAIL_MIT_BEGR_UESS_UNG, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    //deActivateEmployee
    // Negative Tests
    @Test
    void deActivateEmployee_uuid_null() {
        ResponseEntity<String> employeeResponseEntity = this.employeeService.deActivateEmployee(null);

        verify(this.activityRepository, times(0)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.UUID_DARF_NICHT_LEER_SEIN, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void deActivateEmployee_uuid_empty() {
        ResponseEntity<String> employeeResponseEntity = this.employeeService.deActivateEmployee("");

        verify(this.activityRepository, times(0)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.UUID_DARF_NICHT_LEER_SEIN, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void deActivateEmployee_activity_null() {
        String someUUID = "someUUID123";

        Optional<Activity> optionalActivity = Optional.empty();
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optionalActivity);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.deActivateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.ACTIVITY_NICHT_GEFUNDEN_UUID + someUUID, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void deActivateEmployee_activity_employee_schon_deaktiviert() {
        String someUUID = "someUUID123";

        Boolean activated = false;
        Activity activity = new Activity(someUUID, activated, new Employee());
        Optional<Activity> optionalActivity = Optional.of(activity);
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optionalActivity);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.deActivateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.EMPLOYEE_IST_SCHON_DEAKTIVIERT, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void deActivateEmployee_activity_employee_deaktiverung_klappt_nicht() {
        String someUUID = "someUUID123";

        Boolean activated = true;
        Activity activity = new Activity(someUUID, activated, new Employee());
        Optional<Activity> optionalActivity = Optional.of(activity);
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optionalActivity);
        when(this.activityRepository.deActivate(someUUID)).thenReturn(0);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.deActivateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.DEAKTIVIERUNG_DES_EMPLOYEE_IST_NICHT_GEKLAPPT, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    //deActivateEmployee
    // Positive Tests
    @Test
    void deActivateEmployee() {
        String someUUID = "someUUID123";

        Boolean activated = true;
        Activity activity = new Activity(someUUID, activated, new Employee());
        Optional<Activity> optionalActivity = Optional.of(activity);
        when(this.activityRepository.findActivityByUuid(someUUID)).thenReturn(optionalActivity);
        when(this.activityRepository.deActivate(someUUID)).thenReturn(1);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.deActivateEmployee(someUUID);

        verify(this.activityRepository, times(1)).findActivityByUuid(any(String.class));

        assertEquals(EmployeeService.DER_EMPLOYEE_UUID + someUUID + EmployeeService.IST_ERFOLGREICH_DEAKTIVIERT_DABEI_ERHAELT_ER_EINE_BENACHRICHTIGUNG_PER_E_MAIL, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    //updatePassword
    //Negative Tests
    @Test
    void updatePassword_password_null() {
        String password = null;
        ResponseEntity<String> employeeResponseEntity = this.employeeService.updatePassword(password);

        verify(this.employeeRepository, times(0)).updatePassword(any(String.class), any(String.class));

        assertEquals(EmployeeService.PASSWORD_DARF_NICHT_LEER_SEIN, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void updatePassword_password_empty() {
        String password = "";
        ResponseEntity<String> employeeResponseEntity = this.employeeService.updatePassword(password);

        verify(this.employeeRepository, times(0)).updatePassword(any(String.class), any(String.class));

        assertEquals(EmployeeService.PASSWORD_DARF_NICHT_LEER_SEIN, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void updatePassword_password_min_password_length() {
        String password = "pass";
        ResponseEntity<String> employeeResponseEntity = this.employeeService.updatePassword(password);

        verify(this.employeeRepository, times(0)).updatePassword(any(String.class), any(String.class));

        assertEquals(EmployeeService.PASSWORD_MUSS_MINDESTENS + EmployeeValid.MIN_PASSWORD_LENGTH + EmployeeService.SYMBOLE_ENTHALTEN, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void updatePassword_password_nicht_geandert() {
        String password = "passwordGenugLaenge";

        EmployeeVm employee = new EmployeeVm();
        employee.setLogin("login");
        employee.setPassword("password");
        EmployeeDetailsImpl employeeDetails = new EmployeeDetailsImpl(employee);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(employeeDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(this.passwordEncoder.encode(password)).thenReturn("encodePassword");
        when(this.employeeRepository.updatePassword(any(String.class), any(String.class))).thenReturn(0);
        ResponseEntity<String> employeeResponseEntity = this.employeeService.updatePassword(password);

        verify(this.employeeRepository, times(1)).updatePassword(any(String.class), any(String.class));

        assertEquals(EmployeeService.PASSWORD_WURDE_NICHT_GEAENDERT, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    //updatePassword
    //Positive Tests
    @Test
    void updatePassword() {
        String password = "passwordGenugLaenge";

        EmployeeVm employee = new EmployeeVm();
        employee.setLogin("login");
        employee.setPassword("password");
        EmployeeDetailsImpl employeeDetails = new EmployeeDetailsImpl(employee);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(employeeDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(this.passwordEncoder.encode(password)).thenReturn("encodePassword");
        when(this.employeeRepository.updatePassword(any(String.class), any(String.class))).thenReturn(1);
        ResponseEntity<String> employeeResponseEntity = this.employeeService.updatePassword(password);

        verify(this.employeeRepository, times(1)).updatePassword(any(String.class), any(String.class));

        assertEquals(EmployeeService.PASSWORD_WURDE_ERFOLGREICH_GEAENDERT, employeeResponseEntity.getBody());

        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // login()
    //Negative Tests
    @Test
    void logIn_employee_null() {
        EmployeeVm employeeVm = null;
        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.EMPLOYEE_DARF_NICHT_NULL_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void login_employeeValid_employee_id_not_empty() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setId(1L);
        employeeVm.setLogin("musterLogin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.ID_WIRD_AUTOMATISCH_GENERIERT_MAN_MUSS_DA_NICHTS_EINGEBEN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void login_employeeValid_employee_login_equals_null() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin(null);
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.LOGIN_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());
        assertThatNoException();
    }

    @Test
    void login_employeeValid_employee_login_equals_leerString() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.LOGIN_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }


    @Test
    void login_employeeValid_employee_login_equals_stringContainsNull() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("StringNuLLString");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.LOGIN_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void login_employeeValid_employee_loginLength_kleiner_min_login_length() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("Strin");
        employeeVm.setPassword("musterPassword");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.LOGIN_MUSS_MINDESTENS +
                EmployeeValid.MIN_LOGIN_LENGTH +
                EmployeeValid.SYMBOLE_ENTHALTEN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void login_employeeValid_employee_password_equals_null() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword(null);
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.PASSWORD_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());
        assertThatNoException();
    }

    @Test
    void login_employeeValid_employee_password_equals_leer_string() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.PASSWORD_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void login_employeeValid_employee_password_contains_null() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("StringNuLLString");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.PASSWORD_DARF_WEDER_NULL_NOCH_LEER_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void login_employeeValid_employee_password_minlaenge() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("String");
        employeeVm.setPassword("Stri");
        employeeVm.setEmail("musteremail@email.de");

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);
        assertEquals(EmployeeValid.PASSWORD_MUSS_MINDESTENS + EmployeeValid.MIN_PASSWORD_LENGTH +
                EmployeeValid.SYMBOLE_ENTHALTEN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void login_employee_activated() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("StringLogin");
        employeeVm.setPassword("StringPassword");
        employeeVm.setEmail("musteremail@email.de");
        Boolean activated = false;
        Activity activity = new Activity("UUID", activated, null);
        employeeVm.setActivity(activity);

        EmployeeDetailsImpl employeeDetails = new EmployeeDetailsImpl(employeeVm);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(employeeDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authenticationToken);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);

        assertEquals(EmployeeService.EMPLOYEE_IST_NICHT_AKTIVIERT, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // login()
    //Positive Tests
    @Test
    void logIn() {
        EmployeeVm employeeVm = new EmployeeVm();
        employeeVm.setLogin("StringLogin");
        employeeVm.setPassword("StringPassword");
        employeeVm.setEmail("musteremail@email.de");
        Boolean activated = true;
        Activity activity = new Activity("UUID", activated, null);
        employeeVm.setActivity(activity);

        EmployeeDetailsImpl employeeDetails = new EmployeeDetailsImpl(employeeVm);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(employeeDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authenticationToken);

        String jwt = "jwt";
        when(this.jwtUtils.createAccessToken(any(EmployeeVm.class))).thenReturn(jwt);
        HttpCookie httpCookie = new HttpCookie("name", "value");
        when(this.cookieUtils.createJwtCookie(jwt)).thenReturn(httpCookie);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logIn(employeeVm);

        assertEquals(EmployeeService.EMPLOYEE_HAT_SICH_ERFOLGREICH_EINGELOGGT, employeeResponseEntity.getBody());
        assertEquals(httpCookie.toString(), employeeResponseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        org.assertj.core.api.Assertions.assertThatNoException();
    }


    @Test
    void logOut() {
        HttpCookie cookie = new HttpCookie("name", "value");
        when(this.cookieUtils.deleteJwtCookie()).thenReturn(cookie);

        ResponseEntity<String> employeeResponseEntity = this.employeeService.logOut();

        assertEquals(EmployeeService.EMPLOYEE_HAT_SICH_ERFOLGREICH_AUSGELOGGT, employeeResponseEntity.getBody());
        assertEquals(cookie.toString(), employeeResponseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // resendActivateEmail()
    //Negative Tests
    @Test
    void resendActivateEmail_usernameOrEmail_null() {
        ResponseEntity<String> employeeResponseEntity = this.employeeService.resendActivateEmail(null);

        assertEquals(EmployeeService.WEDER_LOGIN_NOCH_E_MAIL_DUERFEN_LEER_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void resendActivateEmail_usernameOrEmail_empty() {
        ResponseEntity<String> employeeResponseEntity = this.employeeService.resendActivateEmail("");

        assertEquals(EmployeeService.WEDER_LOGIN_NOCH_E_MAIL_DUERFEN_LEER_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void resendActivateEmail_usernameOrEmail_activated() {
        String usernameMuster = "usernameMuster";
        Boolean activated = true;
        EmployeeVm employeeVm = new EmployeeVm();
        Activity activity = new Activity("UUID", activated, null);
        employeeVm.setActivity(activity);
        EmployeeDetailsImpl employeeDetails = new EmployeeDetailsImpl(employeeVm);

        when(this.employeeDetailsService.loadUserByUsername(usernameMuster)).thenReturn(employeeDetails);
        ResponseEntity<String> employeeResponseEntity = this.employeeService.resendActivateEmail(usernameMuster);

        assertEquals(EmployeeService.EMPLOYEE_IST_SCHON_AKTIVIERT_SIE_BRAUCHEN_KEINE_REAKTIVIERUNG, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // resendActivateEmail()
    //Positive Tests
    @Test
    void resendActivateEmail() {
        String usernameMuster = "usernameMuster";
        Boolean activated = false;
        EmployeeVm employeeVm = new EmployeeVm();
        Activity activity = new Activity("UUID", activated, null);
        employeeVm.setActivity(activity);
        EmployeeDetailsImpl employeeDetails = new EmployeeDetailsImpl(employeeVm);

        when(this.employeeDetailsService.loadUserByUsername(usernameMuster)).thenReturn(employeeDetails);
        ResponseEntity<String> employeeResponseEntity = this.employeeService.resendActivateEmail(usernameMuster);

        assertEquals(EmployeeService.EMAIL_MIT_AKTIVIERUNGSLINK, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // sendResetPasswordEmail()
    // Negative Tests
    @Test
    void sendResetPasswordEmail_userdetails_null() {
        ResponseEntity<String> employeeResponseEntity = this.employeeService.sendResetPasswordEmail(null);

        assertEquals(EmployeeService.E_MAIL_DARF_NICHT_LEER_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void sendResetPasswordEmail_userdetails_empty() {
        ResponseEntity<String> employeeResponseEntity = this.employeeService.sendResetPasswordEmail("");

        assertEquals(EmployeeService.E_MAIL_DARF_NICHT_LEER_SEIN, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // sendResetPasswordEmail()
    // Positive Tests
    @Test
    void sendResetPasswordEmail() {
        String userDetails = "userdetails";
        EmployeeVm employee = new EmployeeVm();
        EmployeeDetailsImpl employeeDetails = new EmployeeDetailsImpl(employee);
        when(this.employeeDetailsService.loadUserByUsername(userDetails)).thenReturn(employeeDetails);
        ResponseEntity<String> employeeResponseEntity = this.employeeService.sendResetPasswordEmail(userDetails);

        assertEquals(EmployeeService.RESET_PASSWORD_EMAIL, employeeResponseEntity.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }
}
