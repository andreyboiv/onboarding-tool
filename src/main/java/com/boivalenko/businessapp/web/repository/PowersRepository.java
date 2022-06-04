package com.boivalenko.businessapp.web.repository;

import com.boivalenko.businessapp.web.entity.Powers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowersRepository extends JpaRepository<Powers, Long> {
}