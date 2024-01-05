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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;


//@EnableWebSecurity(debug = true) .
// in Produktion soll debug=false sein

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
@EnableAsync
@RequiredArgsConstructor
public class SpringConfig {

    // Client's URL
    @Value("${client.url}")
    private String clientURL;

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
        http
                //Wird Sessionsbehalten auf dem Server ausgeschalten, weil der Parameter STATELESS.
                //Der Client wird Restful API vom Server ansprechen und
                //wird damit das Token mit seiner Information übergeben
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //falls React, Angular usw. verwendet werden,
                // kann/soll dieser Parameter aus.
                // Ansonsten werden keine Requests funktionieren
                .csrf(csrf -> csrf.disable())

                //Authorization Form ausschalten
                .formLogin(formlogin -> formlogin.disable())

                //Browser Standard Authorization Form ausschalten
                .httpBasic(httpBasic -> httpBasic.disable())

                //Wird unbedingt HTTPS verwendet bei allen Requests
                .requiresChannel(r -> r.anyRequest().requiresSecure())

                //Filter in FilterChain hinzugefügt. Der Filter wird VOR dem SessionManagementFilter ausgeführt
                .addFilterBefore(this.authTokenFilter, SessionManagementFilter.class)

                //wird dadurch alle Exceptions gefangen in allen Filter, die danach stehen...
                .addFilterBefore(this.exceptionHandlerFilter, AuthTokenFilter.class);

        return http.build();
    }

    // Es muss den Aufruf des AuthTokenFilter-Filters für den Servlet-Container deaktivieren (damit der Filter nicht zweimal,
    // sondern nur einmal vom Spring-Container aufgerufen wird).
    // https://stackoverflow.com/questions/39314176/filter-invoke-twice-when-register-as-spring-bean
  /*  @Bean
    public FilterRegistrationBean registration(AuthTokenFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter); // FilterRegistrationBean -Filterlogger für Servlet-Container
        registration.setEnabled(false); // Deaktivieren des Filters für den Servlet-Container
        return registration;
    }
    */

    /*    Mit WebMvcConfigurer kann man globale CORS-Regeln
    für alle Controller/Methoden gleichzeitig festlegen
    https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
          Bei Bedarf kann man in jedem
          Controller Ihre eigenen Einstellungen vornehmen
  */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.
                        addMapping("/**"). // für alle URLs
                        allowedOrigins(clientURL). //Von welchen Adressen Anfragen zugelassen werden sollen (kann durch Kommas getrennt angegeben werden)
                        allowCredentials(true). // Es wird erlaubt, das Senden von Cookies für standortübergreifende Anfragen zu machen
                        allowedHeaders("*"). // Alle Header zulassen – funktioniert ohne diese Einstellung in einigen Browsern möglicherweise nicht
                        allowedMethods("*"); // Alle Methoden sind erlaubt (GET, POST, etc.) - ohne diese Einstellung funktioniert CORS nicht!
            }
        };
    }

}
