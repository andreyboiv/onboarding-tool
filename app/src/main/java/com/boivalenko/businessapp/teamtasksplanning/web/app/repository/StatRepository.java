package com.boivalenko.businessapp.teamtasksplanning.web.app.repository;

import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Stat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {
    Stat findByEmployeeToStatEmail(String email);
}
