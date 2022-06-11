package com.boivalenko.businessapp.web.auth.filter;

import com.boivalenko.businessapp.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.web.auth.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final CookieUtils cookieUtils;
    private final JwtUtils jwtUtils;

    // Das ist Open API -
    // URL's, die keine Autorisation brauchen
    // Da werden keine Cookie und JWT validierung benötigt
    private final static List<String> permitURL = Arrays.asList(
            "register",
            "login",
            "activate-account"
    );

    public AuthTokenFilter(CookieUtils cookieUtils, JwtUtils jwtUtils) {
        this.cookieUtils = cookieUtils;
        this.jwtUtils = jwtUtils;
    }

    //Dieser Method wird jedes Mal automatisch bei jedem Request ausgeführt
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isRequestToPublicAPI = false;
        for (String url : this.permitURL) {
            if (request.getRequestURI().toLowerCase().contains(url)) {
                isRequestToPublicAPI = true;
                break;
            }
        }

        if (isRequestToPublicAPI == false
            //   && SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            {
                String jwt = this.cookieUtils.getCookieAccessToken(request);

                if (jwt != null) {

                    if (this.jwtUtils.validate(jwt)) {

                        // TODO
                        //  Man muss hier alle UserDetails (außer password)
                        //  ermittelt werden aus JWT und die müssen
                        //  in Spring Container hinzugefügt werden

                    } else {
                        throw new JwtException("JWT ist ungültig");
                    }

                } else {
                    throw new AuthenticationCredentialsNotFoundException("Token nicht gefunden");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
