package com.boivalenko.businessapp.teamtasksplanning.web.auth.config;


import com.boivalenko.businessapp.teamtasksplanning.web.auth.filter.AuthTokenFilter;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.filter.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SessionManagementFilter;


//@EnableWebSecurity(debug = true) .
// in Produktion soll debug=false sein

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SpringConfig {
    private final AuthTokenFilter authTokenFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Wird Sessionsbehalten auf dem Server ausgeschalten, weil der Parameter STATELESS.
        //Der Client wird Restful API vom Server ansprechen und
        // wird damit das Token mit seiner Information übergeben
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //falls React, Angular usw. verwendet werden,
        // kann/soll dieser Parameter aus.
        // Ansonsten werden keine Requests funktionieren
        http.csrf().disable();

        //Authorization Form ausschalgen
        http.formLogin().disable();

        //Browser Standard Authorization Form ausschlaten
        http.httpBasic().disable();

        //Wird unbedingt HTTPS verwendet bei allen Requests
        http.requiresChannel().anyRequest().requiresSecure();

        //Filter in FilterChain hinzugefügt. Der Filter wird VOR dem SessionManagementFilter ausgeführt 
        http.addFilterBefore(this.authTokenFilter, SessionManagementFilter.class);

        //wird dadurch alle Exceptions gefangen in allen Filter, die danach stehen...
        http.addFilterBefore(this.exceptionHandlerFilter, AuthTokenFilter.class);

        return http.build();
    }
}


