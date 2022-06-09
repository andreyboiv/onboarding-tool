package com.boivalenko.businessapp.web.auth.config;


import com.boivalenko.businessapp.web.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//@EnableWebSecurity(debug = true) .
// in Produktion soll debug=false sein

@Configuration
@EnableWebSecurity(debug = true)
public class SpringConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //Einweg-Hash-Passwort-Encoder : Bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Für Login und Password prüfen
    //Method ist in der Spring Security Doku beschrieben
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //Method ist in der Spring Security Doku beschrieben
    //Da werden einige Einstellungen für
    // AuthenticationManager eingesetzt
    // für richtige Login und Password Encoding.
    // Sehr wichtiger Method. Ohne dieser Method wird
    //  UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    //  in AuthController nicht funktionieren
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

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
    }

}


