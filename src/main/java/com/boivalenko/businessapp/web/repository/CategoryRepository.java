package com.boivalenko.businessapp.web.repository;

import com.boivalenko.businessapp.web.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}