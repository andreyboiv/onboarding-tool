package com.boivalenko.businessapp.web.repository;

import com.boivalenko.businessapp.web.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}