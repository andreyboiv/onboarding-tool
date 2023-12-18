package com.boivalenko.businessapp.teamtasksplanning.web.auth.filter;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.service.EmployeeDetailsImpl;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final CookieUtils cookieUtils;
    private final JwtUtils jwtUtils;

    // Das ist Open API -
    // URL's, die keine Autorisation brauchen
    // Da werden keine Cookies und JWT validierung benötigt
    private final static List<String> permitURL = Arrays.asList(
            "register",
            "activate-account",
            "login",
            "deactivate-account"
    );

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
                String jwt = this.cookieUtils.getCookieAccessToken(request);

                if (jwt != null) {

                    if (this.jwtUtils.validate(jwt)) {

                        //Employee kommt aus dem Token
                        Employee employee = this.jwtUtils.getEmployee(jwt);

                        EmployeeDetailsImpl userDetails = new EmployeeDetailsImpl(employee);

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

                        // Request wird in Container hinzugefügt,
                        // dass der Container die Authentication Daten erhält
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Objet Authentification wird in Spring Container hinzugefügt.
                        // Damit Spring versteht, dass der Employee sich erfolgreich eingeloggt hat
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    } else {
                        throw new JwtException("JWT ist ungültig");
                    }

                } else {
                    throw new AuthenticationCredentialsNotFoundException("Token nicht gefunden");
                }
            }

        filterChain.doFilter(request, response);
    }
}
