package com.boivalenko.businessapp.web.auth.service;

import com.boivalenko.businessapp.web.auth.entity.Employee;
import com.boivalenko.businessapp.web.auth.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String userDetails) throws UsernameNotFoundException {

        Optional<Employee> employeeOptional = this.employeeRepository.findEmployeeByLogin(userDetails);
        if (!employeeOptional.isPresent()) {
            employeeOptional = this.employeeRepository.findEmployeeByEmail(userDetails);
        }
        if (!employeeOptional.isPresent()) {
            throw new UsernameNotFoundException("User nicht gefunden. Login or Email: " + userDetails);
        }

        return new UserDetailsImpl(employeeOptional.get());
    }
}
