package com.boivalenko.businessapp.onboarding.web.auth.service;

import com.boivalenko.businessapp.onboarding.web.auth.entity.Powers;
import com.boivalenko.businessapp.onboarding.web.auth.viewmodel.EmployeeVm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


// Für Behalten von EmployeeVm
// (Wrapper für EmployeeVm, der Spring Container versteht)
@Setter
@Getter
@RequiredArgsConstructor
public class EmployeeDetailsImpl implements UserDetails {

    private final EmployeeVm employee;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Powers> powers = this.employee.getPowers();
        Set<SimpleGrantedAuthority> retVal = new HashSet<>();
        if (powers != null) {
            for (Powers power : powers) {
                retVal.add(new SimpleGrantedAuthority(power.getName()));
            }
        }
        return retVal;
    }

    public Long getID() {
        return this.employee.getId();
    }

    public String getEmail() {
        return this.employee.getEmail();
    }

    public Boolean isActivated() {
        return this.employee.getActivity().getActivated();
    }

    @Override
    public String getUsername() {
        return this.employee.getLogin();
    }

    @Override
    public String getPassword() {
        return this.employee.getPassword();
    }


    //Man braucht keine diese Nachprüfung
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Man braucht keine diese Nachprüfung
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Man braucht keine diese Nachprüfung
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Man braucht keine diese Nachprüfung.
    // Die Logik ist realisiert dank Activity.activated Feld
    @Override
    public boolean isEnabled() {
        return true;
    }

}
