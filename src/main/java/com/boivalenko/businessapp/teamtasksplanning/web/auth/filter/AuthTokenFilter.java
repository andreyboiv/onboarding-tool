package com.boivalenko.businessapp.teamtasksplanning.web.auth.filter;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.service.EmployeeDetailsImpl;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.CookieUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.utils.JwtUtils;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel.EmployeeVm;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
    private static final List<String> permitURL = Arrays.asList(
            "register",
            "activate-account",
            "login",
            "resend-activate-email",
            "send-reset-password-email"
    );

    //Dieser Method wird jedes Mal automatisch bei jedem Request ausgeführt
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isRequestToPublicAPI = false;
        for (String url : permitURL) {
            if (request.getRequestURI().toLowerCase().contains(url)) {
                isRequestToPublicAPI = true;
                break;
            }
        }

        // es kam eine Request nicht von Open API. Daher braucht eine Authentication...
        if (!isRequestToPublicAPI
                //&&
        // falls der User keine Authentication durchgeführt wurde. Daher braucht eine Authentication...
           //     SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            String jwt = null;

            if (request.getRequestURI().contains("update-password")) {
                //man kriegt Token aus dem Header
                jwt = this.cookieUtils.getJwtFromHeader(request);
            } else {
                jwt = this.cookieUtils.getCookieAccessToken(request);
            }

                if (jwt != null) {

                    if (this.jwtUtils.validate(jwt)) {

                        //EmployeeVm kommt aus dem Token
                        EmployeeVm employee = this.jwtUtils.getEmployee(jwt);

                        EmployeeDetailsImpl userDetails = new EmployeeDetailsImpl(employee);

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

                        // Request wird in Container hinzugefügt,
                        // dass der Container die Authentication Daten erhält
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Objet Authentification wird in Spring Container hinzugefügt.
                        // Damit Spring versteht, dass der EmployeeVm sich erfolgreich eingeloggt hat
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
