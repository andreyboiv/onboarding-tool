package com.boivalenko.businessapp.web.auth.utils;

import com.boivalenko.businessapp.web.auth.entity.Employee;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Level;

@Log
@Component
public class JwtUtils {

    //Secret Key für JWT Token Erzeugen
    @Value("${jwt.secret}")
    private String jwtSecret;

    //JWT Name "access_token" (hat mit OAuth2 nichts zu tun).
    // Dauer vom Token für automatische Loging (1 Tag = 86400000 ms)
    // Alle Request werden automatisch durch
    // Authentication durchgeführt.
    @Value("${jwt.access_token-expiration}")
    private int accessTokenExpiration;


    public String createAccessToken(Employee employee) {
        Date currentDate = new Date();
        return Jwts.builder()
                .setSubject(employee.getId().toString())
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + accessTokenExpiration)) // Gültigkeit vom Token
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact(); //wird in Base64 Format konvertiert
    }

    public boolean validate(String jwt) {
        try {
            Jwts.
                    parser(). //jwt format validierung
                    setSigningKey(jwtSecret). //jwt validierung aufgrund jwtSecret Key
                    parseClaimsJws(jwt); //jwt signature validierung
            return true;
        } catch (MalformedJwtException e) {
            log.log(Level.SEVERE, "Ungültiger JWT Token:", jwt);
        } catch (ExpiredJwtException e) {
            log.log(Level.SEVERE, "JWT Token abgelaufen:", jwt);
        } catch (UnsupportedJwtException e) {
            log.log(Level.SEVERE, "JWT Token wird nicht unterstützt:", jwt);
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, "JWT unkorrekt", jwt);
        }

        return false;
    }


}
