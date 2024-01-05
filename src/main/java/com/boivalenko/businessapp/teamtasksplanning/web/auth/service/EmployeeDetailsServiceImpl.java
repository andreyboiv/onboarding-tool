package com.boivalenko.businessapp.teamtasksplanning.web.auth.service;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.repository.EmployeeRepository;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel.EmployeeVm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeDetailsServiceImpl implements UserDetailsService {

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

        return this.getEmployeeDetails(employeeOptional);
    }

    private static EmployeeDetailsImpl getEmployeeDetails(Optional<Employee> employeeOptional) {
        Employee employee = employeeOptional.get();

        EmployeeVm employeeVm = new EmployeeVm();

        employeeVm.setId(employee.getId());
        employeeVm.setLogin(employee.getLogin());
        employeeVm.setPassword(employee.getPassword());
        employeeVm.setEmail(employee.getEmail());
        employeeVm.setPowers(employee.getPowers());
        employeeVm.setActivity(employee.getActivity());

        return new EmployeeDetailsImpl(employeeVm);
    }
}
