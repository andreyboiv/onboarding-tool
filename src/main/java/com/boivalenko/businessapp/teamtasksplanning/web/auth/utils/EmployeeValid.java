package com.boivalenko.businessapp.teamtasksplanning.web.auth.utils;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel.EmployeeVm;

import java.util.regex.Pattern;

public final class EmployeeValid {

    // Regex laut RFC822
    public static final String EMAIL_REGEX_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MIN_LOGIN_LENGTH = 6;
    public static final String EMPLOYEE_DARF_NICHT_NULL_SEIN = "Employee darf nicht null sein";
    public static final String ID_WIRD_AUTOMATISCH_GENERIERT_MAN_MUSS_DA_NICHTS_EINGEBEN = "ID wird automatisch generiert. Man muss da nichts eingeben";
    public static final String LOGIN_DARF_WEDER_NULL_NOCH_LEER_SEIN = "LOGIN darf weder NULL noch leer sein";
    public static final String LOGIN_MUSS_MINDESTENS = "LOGIN muss mindestens ";
    public static final String SYMBOLE_ENTHALTEN = " Symbole enthalten";
    public static final String PASSWORD_DARF_WEDER_NULL_NOCH_LEER_SEIN = "PASSWORD darf weder NULL noch leer sein";
    public static final String PASSWORD_MUSS_MINDESTENS = "PASSWORD muss mindestens ";
    public static final String EMAIL_DARF_WEDER_NULL_NOCH_LEER_SEIN = "EMAIL darf weder NULL noch leer sein";
    public static final String EMAIL_FORMAT_IST_UNKORREKT = "EMAIL Format ist unkorrekt";

    private EmployeeValid(){
        throw new AssertionError("Es dürfen keine Objekte des Klasses erzeugt werden");
    }

    public static String isEmployeeValid(EmployeeVm employee) {

        String error = "";

        String loginAndPasswordError = isValidLoginAndPassword(employee);
        if (!loginAndPasswordError.isEmpty()) {
            error = loginAndPasswordError;
        }

        else if (employee.getEmail() == null || employee.getEmail().isEmpty() || employee.getEmail().toLowerCase().contains("null")) {
            error = EMAIL_DARF_WEDER_NULL_NOCH_LEER_SEIN;
        }

        //E-mail Validation
        else if (!isValidEmailAddress(employee.getEmail())) {
            error = EMAIL_FORMAT_IST_UNKORREKT;
        }

        return error;
    }

    private static boolean isValidEmailAddress(String email) {
        return patternMatches(email, EMAIL_REGEX_PATTERN);
    }

    private static boolean patternMatches(String email, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public static String isValidLoginAndPassword(EmployeeVm employeeVm) {
        String error = "";

        if (employeeVm == null) {
            error = EMPLOYEE_DARF_NICHT_NULL_SEIN;
        }

        else if (employeeVm.getId() != null) {
            error = ID_WIRD_AUTOMATISCH_GENERIERT_MAN_MUSS_DA_NICHTS_EINGEBEN;
        }

        else if (employeeVm.getLogin() == null || employeeVm.getLogin().isEmpty() || employeeVm.getLogin().toLowerCase().contains("null")) {
            error = LOGIN_DARF_WEDER_NULL_NOCH_LEER_SEIN;
        }

        else if (employeeVm.getLogin().length() < MIN_LOGIN_LENGTH) {
            error = LOGIN_MUSS_MINDESTENS + MIN_LOGIN_LENGTH + SYMBOLE_ENTHALTEN;
        }

        else if (employeeVm.getPassword() == null || employeeVm.getPassword().isEmpty() || employeeVm.getPassword().toLowerCase().contains("null")) {
            error = PASSWORD_DARF_WEDER_NULL_NOCH_LEER_SEIN;
        }

        else if (employeeVm.getPassword().length() < MIN_PASSWORD_LENGTH) {
            error = PASSWORD_MUSS_MINDESTENS + MIN_PASSWORD_LENGTH + SYMBOLE_ENTHALTEN;
        }

        return error;
    }
}
