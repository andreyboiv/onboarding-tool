package com.boivalenko.businessapp.web.auth.utils;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    private final String ACCESS_TOKEN = "access_token";

    @Value("${cookie.jwt.max-age}")
    private int cookieAccessTokenDuration;

    @Value("${cookie.server.domain}")
    private String cookieAccessTokenDomain;

    // Cookie wird NUR auf dem Server erzeugt (server-side cookie)
    // mit jwt als Value und auf dem Server wird er validiert
    public HttpCookie createJwtCookie(String jwt) { // jwt - Value für Cookie
        return ResponseCookie.
        from(ACCESS_TOKEN, jwt) //Name und Bedeutung von Cookie
                .maxAge(cookieAccessTokenDuration)
                .sameSite(SameSiteCookies.STRICT.getValue())
                .httpOnly(true)
                .secure(true) //Cookie wird von Client an Server übergeben nur im Fall von https
                .domain(cookieAccessTokenDomain) //für welche domain wird Cookie valid
                .path("/") //Cookie wird für jede URl vom Server verfügbar
                .build();

    }

}
