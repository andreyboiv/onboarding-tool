package com.boivalenko.businessapp.teamtasksplanning.web.auth.config;


import com.boivalenko.businessapp.teamtasksplanning.web.auth.filter.AuthTokenFilter;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.filter.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
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

import java.util.Properties;


//@EnableWebSecurity(debug = true) .
// in Produktion soll debug=false sein

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAsync
@RequiredArgsConstructor
public class SpringConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String login;

    @Value("${spring.mail.password}")
    private String password;

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
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.host);
        mailSender.setPort(this.port);

        mailSender.setUsername(this.login);
        mailSender.setPassword(this.password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
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


