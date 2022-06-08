package com.boivalenko.businessapp.web.authorization.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


//@EnableWebSecurity(debug = true) .
// in Produktion soll debug=false sein

@Configuration
@EnableWebSecurity(debug = true)
public class SpringConfig extends WebSecurityConfigurerAdapter {

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


