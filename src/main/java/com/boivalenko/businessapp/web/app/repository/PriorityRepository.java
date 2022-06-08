package com.boivalenko.businessapp.web.app.repository;

import com.boivalenko.businessapp.web.app.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    List<Priority> findByEmployeesToPriorityEmailOrderByTitleAsc(String email);

    @Query("SELECT c FROM Priority c where " +
            "(:title is null or :title='' " +
            " or lower(c.title) like lower(concat('%', :title,'%'))) " +
            " and c.employeesToPriority.email=:email  " +
            " order by c.title asc")
    List<Priority> findAllByEmailQuery(@Param("title") String title, @Param("email") String email);

}