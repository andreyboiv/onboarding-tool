package com.boivalenko.businessapp.web.repository;

import com.boivalenko.businessapp.web.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}