package com.boivalenko.businessapp.onboarding.web.app.repository;

import com.boivalenko.businessapp.onboarding.web.app.entity.Category;
import com.boivalenko.businessapp.onboarding.web.auth.entity.Employee;
import com.boivalenko.businessapp.onboarding.web.auth.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
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

    public static final String LOGIN_MUSTER_REPOSITORY_TEST = "LoGiN_test234234234.de";
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void initUseCase() {
        Employee employee = new Employee(LOGIN_MUSTER_REPOSITORY_TEST, "passwordMuster", "andrey@muster.de", null, null);
        this.employeeRepository.save(employee);
        this.categoryErstellen("Einarbeitung", employee);
        this.categoryErstellen("Teammeetings", employee);
        this.categoryErstellen("Haupttasks", employee);
    }

    private Category categoryErstellen(String title, Employee employee) {
        Category category = new Category();
        category.setTitle(title);
        category.setCompletedCount(0L);
        category.setEmployeesToCategory(employee);
        this.categoryRepository.save(category);
        return category;
    }

    // findByEmployeesToCategoryEmailOrderByTitleAsc
    // Negative Tests
    @Test
    @Transactional
    void findByEmployeesToCategoryLoginOrderByTitleAsc_login_null() {
        String login = null;
        List<Category> list = this.categoryRepository.findByEmployeesToCategoryLoginOrderByIdAsc(login);
        Assertions.assertTrue(list.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    @Transactional
    void findByEmployeesToCategoryLoginOrderByTitleAsc_login_leer() {
        String login = "";
        List<Category> list = this.categoryRepository.findByEmployeesToCategoryLoginOrderByIdAsc(login);
        Assertions.assertTrue(list.isEmpty());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findByEmployeesToCategoryLoginOrderByTitleAsc
    // Positive Tests
    @Test
    @Transactional
    void findByEmployeesToCategoryLoginOrderByTitleAsc() {
        List<Category> list = this.categoryRepository.findByEmployeesToCategoryLoginOrderByIdAsc(LOGIN_MUSTER_REPOSITORY_TEST);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(list.get(0).getEmployeesToCategory().getLogin(), LOGIN_MUSTER_REPOSITORY_TEST);
        org.assertj.core.api.Assertions.assertThatNoException();
    }
}
