package com.boivalenko.businessapp.web.repository;

import com.boivalenko.businessapp.web.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
}