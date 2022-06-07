package com.boivalenko.businessapp.web.repository;

import com.boivalenko.businessapp.web.entity.Stat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {
    Stat findByEmployeeToStatEmail(String email);
}