package com.boivalenko.businessapp.teamtasksplanning.web.auth.utils;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel.EmployeeVm;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Log
@Component
public class JwtUtils {

    private EmployeeVm employeeVm;

    public static final String CLAIM_EMPLOYEE_KEY = "employee";
    //Secret Key für JWT Token Erzeugen
    @Value("${jwt.secret}")
    private String jwtSecret;

    //JWT Name "access_token" (hat mit OAuth2 nichts zu tun).
    // Dauer vom Token für automatische Loging (1 Tag = 86400000 ms)
    // alle Request werden automatisch durch
    // Authentication durchgeführt.
    @Value("${jwt.access_token-expiration}")
    private int accessTokenExpiration;

    @Value("${jwt.resetPasswordTokenExpiration}")
    private int resetPasswordTokenExpiration;

    public String createAccessToken(EmployeeVm employee) {
        return this.createToken(employee, accessTokenExpiration);
    }

    //es wird JWT für Password Reset generiert
    public String createEmailResetToken(EmployeeVm employee) {
        return this.createToken(employee, resetPasswordTokenExpiration);
    }

    private String createToken(EmployeeVm employee, int duration) {
        Date currentDate = new Date();

        employee.setPassword(null);
        employee.setEmail(null);
        employee.setActivity(null);

        Map claims = new HashMap<String, Object>();
        claims.put(CLAIM_EMPLOYEE_KEY, employee);
        //claims.put(Claims.SUBJECT, employee.getId());

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + duration)) // Gültigkeit vom Token
                .signWith(SignatureAlgorithm.HS512, key)
                .compact(); //wird in Base64 Format konvertiert
    }

    public boolean validate(String jwt) {
        try {
            // https://github.com/jwtk/jjwt

            Map map = (Map)Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8)).
                            parseClaimsJws(jwt).getBody().
                            get(CLAIM_EMPLOYEE_KEY);

            ObjectMapper mapper = new ObjectMapper();
            this.employeeVm = mapper.convertValue(map, EmployeeVm.class);

            return true;
        } catch (MalformedJwtException e) {
            log.log(Level.SEVERE, "Invalid JWT token:", jwt);
        } catch (ExpiredJwtException e) {
            log.log(Level.SEVERE, "JWT token is expired:", jwt);
        } catch (UnsupportedJwtException e) {
            log.log(Level.SEVERE, "JWT token is unsupported:", jwt);
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, "JWT claims string is empty:", jwt);
        }

        return false;
    }

    public EmployeeVm getEmployee(String jwt) {
        return employeeVm;
    }
}
