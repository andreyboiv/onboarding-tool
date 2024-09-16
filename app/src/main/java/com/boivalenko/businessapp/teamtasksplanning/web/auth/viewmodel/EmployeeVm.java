package com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Activity;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Powers;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.viewmodel.support.AbstractViewModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EmployeeVm extends AbstractViewModel<Long> {
    private String login;
    private String password;
    private String email;
    private Set<Powers> powers = new HashSet<>();
    private Activity activity;
}
