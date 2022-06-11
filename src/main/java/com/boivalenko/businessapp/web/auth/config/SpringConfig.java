package com.boivalenko.businessapp.web.auth.config;


import com.boivalenko.businessapp.web.auth.filter.AuthTokenFilter;
import com.boivalenko.businessapp.web.auth.filter.ExceptionHandlerFilter;
import com.boivalenko.businessapp.web.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;


//@EnableWebSecurity(debug = true) .
// in Produktion soll debug=false sein

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private AuthTokenFilter authTokenFilter;

    @Autowired
    public void setAuthTokenFilter(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }

    // Man muss den Aufruf von dem AuthTokenFilter für den Servlet-Container deaktivieren
    // (damit der Filter nicht 2 Mal aufgerufen wird, sondern nur einmal aus dem Spring-Container)
    //https://stackoverflow.com/questions/39314176/filter-invoke-twice-when-register-as-spring-bean
    public FilterRegistrationBean filterRegistrationBean(AuthTokenFilter filter){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false); //wird die Verwendung des Filters für den Servlet Container ausgeschaltet
        return registrationBean;
    }

    private ExceptionHandlerFilter exceptionHandlerFilter;

    @Autowired
    public void setExceptionHandlerFilter(ExceptionHandlerFilter exceptionHandlerFilter) {
        this.exceptionHandlerFilter = exceptionHandlerFilter;
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

        //Wird Sessionsbehalten auf dem Server ausgeschalten, weil der Parameter STATELESS.
        //Der Client wird Restful API vom Server ansprechen und
        // wird damit das Token mit seinem Info übergeben
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
    }

}


