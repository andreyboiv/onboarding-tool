package com.boivalenko.businessapp.web.repository;

import com.boivalenko.businessapp.web.entity.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
}