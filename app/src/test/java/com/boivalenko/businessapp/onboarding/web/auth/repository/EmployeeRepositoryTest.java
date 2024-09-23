package com.boivalenko.businessapp.onboarding.web.auth.repository;

import com.boivalenko.businessapp.onboarding.web.auth.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    public static final String EMAIL_MUSTER_REPOSITORY_TEST = "email@muster_repository_test234234234.de".toLowerCase();
    public static final String LOGIN_MUSTER = "loginMuster".toLowerCase();
    public static final String PASSWORD_MUSTER = "passwordMuster".toLowerCase();
    public static final String UUID = "e58ed723-918q-4157-bee9-fdbzaadc15f5".toLowerCase();

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void initUseCase() {
        Employee employee = new Employee(LOGIN_MUSTER, PASSWORD_MUSTER, EMAIL_MUSTER_REPOSITORY_TEST, null, null);
        this.employeeRepository.save(employee);
    }

    @Test
    void existsEmployeeByLogin_null() {
        boolean exists = this.employeeRepository.existsEmployeeByLoginEqualsIgnoreCase(null);
        Assertions.assertFalse(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void existsEmployeeByLogin_leer() {
        boolean exists = this.employeeRepository.existsEmployeeByLoginEqualsIgnoreCase(LOGIN_MUSTER);
        Assertions.assertTrue(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void existsEmployeeByLogin_nicht_existiert() {
        boolean exists = this.employeeRepository.existsEmployeeByLoginEqualsIgnoreCase(LOGIN_MUSTER + LOGIN_MUSTER);
        Assertions.assertFalse(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void existsEmployeeByLogin() {
        boolean exists = this.employeeRepository.existsEmployeeByLoginEqualsIgnoreCase(LOGIN_MUSTER);
        Assertions.assertTrue(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void existsEmployeeByEmailEqualsIgnoreCase_null() {
        boolean exists = this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(null);
        Assertions.assertFalse(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void existsEmployeeByEmailEqualsIgnoreCase_leer() {
        boolean exists = this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase("");
        Assertions.assertFalse(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void existsEmployeeByEmailEqualsIgnoreCase_case_sensetiv() {
        boolean exists = this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(EMAIL_MUSTER_REPOSITORY_TEST.toUpperCase());
        Assertions.assertTrue(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void existsEmployeeByEmailEqualsIgnoreCase_nicht_existiert() {
        boolean exists = this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(EMAIL_MUSTER_REPOSITORY_TEST + EMAIL_MUSTER_REPOSITORY_TEST);
        Assertions.assertFalse(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void existsEmployeeByEmailEqualsIgnoreCase() {
        boolean exists = this.employeeRepository.existsEmployeeByEmailEqualsIgnoreCase(EMAIL_MUSTER_REPOSITORY_TEST);
        Assertions.assertTrue(exists);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByLogin_null() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByLogin(null);
        Assertions.assertFalse(employeeByLogin.isPresent());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByLogin_leer() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByLogin("");
        Assertions.assertFalse(employeeByLogin.isPresent());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByLogin_case_sensetiv() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByLogin(LOGIN_MUSTER.toUpperCase());
        Assertions.assertFalse(employeeByLogin.isPresent());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByLogin_nicht_existiert() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByLogin(LOGIN_MUSTER + LOGIN_MUSTER);
        Assertions.assertFalse(employeeByLogin.isPresent());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByLogin() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByLogin(LOGIN_MUSTER);
        Assertions.assertTrue(employeeByLogin.isPresent());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByEmail_null() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByEmail(null);
        Assertions.assertTrue(employeeByLogin.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByEmail_leer() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByEmail("");
        Assertions.assertTrue(employeeByLogin.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByEmail_case_sensetiv() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByEmail(EMAIL_MUSTER_REPOSITORY_TEST.toUpperCase());
        Assertions.assertTrue(employeeByLogin.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByEmail_nicht_existiert() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByEmail(EMAIL_MUSTER_REPOSITORY_TEST + EMAIL_MUSTER_REPOSITORY_TEST);
        Assertions.assertTrue(employeeByLogin.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByEmail() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByEmail(EMAIL_MUSTER_REPOSITORY_TEST);
        Assertions.assertTrue(employeeByLogin.isPresent());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByActivity_Uuid_null() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByActivity_Uuid(null);
        Assertions.assertTrue(employeeByLogin.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByActivity_Uuid_leer() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByActivity_Uuid("");
        Assertions.assertTrue(employeeByLogin.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByActivity_Uuid_case_sensetiv() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByActivity_Uuid(UUID.toUpperCase());
        Assertions.assertTrue(employeeByLogin.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findEmployeeByActivity_Uuid_nicht_existiert() {
        Optional<Employee> employeeByLogin = this.employeeRepository.findEmployeeByActivity_Uuid(UUID + UUID);
        Assertions.assertTrue(employeeByLogin.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    //@Query("UPDATE Employee e SET e.password = :password WHERE e.login = :login")

    @Test
    void updatePassword_login_null() {
        int update = this.employeeRepository.updatePassword(null, PASSWORD_MUSTER);
        Assertions.assertEquals(0, update);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void updatePassword_login_leer() {
        int update = this.employeeRepository.updatePassword("", PASSWORD_MUSTER);
        Assertions.assertEquals(0, update);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void updatePassword_login_case_sensetiv() {
        int update = this.employeeRepository.updatePassword(LOGIN_MUSTER.toUpperCase(), PASSWORD_MUSTER);
        Assertions.assertEquals(0, update);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void updatePassword_login_nicht_existiert() {
        int update = this.employeeRepository.updatePassword(LOGIN_MUSTER + LOGIN_MUSTER, "password");
        Assertions.assertEquals(0, update);
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void updatePassword() {
        int update = this.employeeRepository.updatePassword(LOGIN_MUSTER, PASSWORD_MUSTER + PASSWORD_MUSTER);
        Assertions.assertEquals(1, update);
        org.assertj.core.api.Assertions.assertThatNoException();
    }
}
