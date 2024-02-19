package com.boivalenko.businessapp.teamtasksplanning.web.app.repository;

import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByEmployeesToCategoryLoginOrderByTitleAsc(String login);

    @Query("SELECT c FROM Category c where " +
            "(:title is null or :title='' " +
            " or lower(c.title) like lower(concat('%', :title,'%'))) " +
            " and c.employeesToCategory.email=:email  " +
            " order by c.title asc")
    List<Category> findAllByEmailQuery(@Param("title") String title, @Param("email") String email);
}
