package com.boivalenko.businessapp.onboarding.web.app.repository;

import com.boivalenko.businessapp.onboarding.web.app.entity.Stat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {
    Stat findByEmployeeToStatEmail(String email);
}
