package com.boivalenko.businessapp.teamtasksplanning.web.app.repository;

import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Category;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.entity.Employee;
import com.boivalenko.businessapp.teamtasksplanning.web.auth.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    public static final String EMAIL_MUSTER_REPOSITORY_TEST = "email@muster_repository_test234234234.de";
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void initUseCase() {
        Employee employee = new Employee("loginMuster","passwordMuster", EMAIL_MUSTER_REPOSITORY_TEST,null, null);
        this.employeeRepository.save(employee);
    }

    // findByEmployeesToCategoryEmailOrderByTitleAsc
    // Negative Tests
    @Test
    void findByEmployeesToCategoryEmailOrderByTitleAsc_email_null() {
        String email = null;
        List<Category> list = this.categoryRepository.findByEmployeesToCategoryEmailOrderByTitleAsc(email);
        Assertions.assertTrue(list.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findByEmployeesToCategoryEmailOrderByTitleAsc_email_leer() {
        String email = "";
        List<Category> list = this.categoryRepository.findByEmployeesToCategoryEmailOrderByTitleAsc(email);
        Assertions.assertTrue(list.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findByEmployeesToCategoryEmailOrderByTitleAsc
    // Positive Tests
    @Test
    void findByEmployeesToCategoryEmailOrderByTitleAsc() {
        List<Category> list = this.categoryRepository.findByEmployeesToCategoryEmailOrderByTitleAsc(EMAIL_MUSTER_REPOSITORY_TEST);
        Assertions.assertFalse(list.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }
}
