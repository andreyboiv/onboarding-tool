package com.boivalenko.businessapp.onboarding.web.app.repository;

import com.boivalenko.businessapp.onboarding.web.auth.entity.Powers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerRepository extends JpaRepository<Powers, Long> {
}
