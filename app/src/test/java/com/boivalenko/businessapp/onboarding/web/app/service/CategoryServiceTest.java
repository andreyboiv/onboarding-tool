package com.boivalenko.businessapp.onboarding.web.app.service;

import com.boivalenko.businessapp.onboarding.web.app.entity.Category;
import com.boivalenko.businessapp.onboarding.web.app.repository.CategoryRepository;
import com.boivalenko.businessapp.onboarding.web.auth.entity.Employee;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    // Save
    // Negative Tests
    @Test
    void save_auto_id() {
        Category category = new Category("Title", null,
                null, new Employee());
        category.setId(1L);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.save(category);
        Assertions.assertEquals(CategoryService.ID_WIRD_AUTOMATISCH_GENERIERT_MAN_MUSS_DAS_NICHT_EINGEBEN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void save_title_empty() {
        Category category = new Category("", null,
                null, new Employee());
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.save(category);
        Assertions.assertEquals(CategoryService.TITLE_DARF_WEDER_NULL_NOCH_LEER_SEIN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void save_title_contains_null() {
        Category category = new Category("TitleNullwertnull", null,
                null, new Employee());
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.save(category);
        Assertions.assertEquals(CategoryService.TITLE_DARF_WEDER_NULL_NOCH_LEER_SEIN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // Save
    // Positive Tests

    @Test
    void save() {
        Category category = new Category("Title", null,
                null, new Employee());

        when(this.categoryRepository.save(category)).thenReturn(category);

        ResponseEntity<Category> categoryResponseEntity = this.categoryService.save(category);
        Assertions.assertEquals(HttpStatus.OK, categoryResponseEntity.getStatusCode());
        Assertions.assertEquals(category, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(1)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // Update
    // Negative Tests

    @Test
    void update_id_not_null() {
        Category category = new Category("Title", null,
                null, new Employee());
        category.setId(null);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.update(category);
        Assertions.assertEquals(CategoryService.ID_DARF_WEDER_NULL_NOCH_0_SEIN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void update_id_0() {
        Category category = new Category("Title", null,
                null, new Employee());
        category.setId(0L);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.update(category);
        Assertions.assertEquals(CategoryService.ID_DARF_WEDER_NULL_NOCH_0_SEIN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void update_title_empty() {
        Category category = new Category("", null,
                null, new Employee());
        category.setId(1L);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.update(category);
        Assertions.assertEquals(CategoryService.TITLE_DARF_WEDER_NULL_NOCH_LEER_SEIN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void update_title_contains_null() {
        Category category = new Category("Wertnullwert", null,
                null, new Employee());
        category.setId(1L);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.update(category);
        Assertions.assertEquals(CategoryService.TITLE_DARF_WEDER_NULL_NOCH_LEER_SEIN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void update_not_exists_bei_id() {
        Category category = new Category("Title", null,
                null, new Employee());
        category.setId(1L);
        when(categoryRepository.existsById(category.getId())).thenReturn(Boolean.FALSE);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.update(category);

        Assertions.assertEquals(String.format(CategoryService.ID_NICHT_GEFUNDEN, category.getId()), categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // Update
    // Positive Tests

    @Test
    void update() {
        Category category = new Category("Title", null,
                null, new Employee());
        category.setId(1L);
        when(categoryRepository.existsById(category.getId())).thenReturn(true);
        when(this.categoryRepository.save(category)).thenReturn(category);

        ResponseEntity<Category> categoryResponseEntity = this.categoryService.update(category);
        Assertions.assertEquals(true, categoryRepository.existsById(category.getId()));
        Assertions.assertEquals(HttpStatus.OK, categoryResponseEntity.getStatusCode());
        Assertions.assertEquals(category, categoryResponseEntity.getBody());

        verify(this.categoryRepository, times(1)).save(any(Category.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // Delete
    // Negative Tests

    @Test
    void deleteById_id_is_0() {
        Long id = 0L;
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.deleteById(id);

        Assertions.assertEquals(CategoryService.ID_DARF_NICHT_0_SEIN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).deleteById(any(Long.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void deleteById_not_exists_bei_id() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(false);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.deleteById(id);
        Assertions.assertEquals(false, categoryRepository.existsById(id));

        Assertions.assertEquals(String.format(CategoryService.ID_NICHT_GEFUNDEN, id), categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).deleteById(any(Long.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // Delete
    // Positive Tests

    @Test
    void deleteById() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.deleteById(id);
        Assertions.assertEquals(true, categoryRepository.existsById(id));
        Assertions.assertEquals(HttpStatus.OK, categoryResponseEntity.getStatusCode());

        Assertions.assertEquals(new Gson().toJson(CategoryService.CATEGORY_MIT_ID + id + CategoryService.ERFOLGREICH_GELOESCHT), categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(1)).deleteById(any(Long.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findById
    // Negative Tests

    @Test
    void findById_id_is_0() {
        Long id = 0L;
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.findById(id);
        Assertions.assertEquals(CategoryService.ID_DARF_NICHT_0_SEIN, categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).findById(any(Long.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findById_not_exists_bei_id() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(false);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.findById(id);
        Assertions.assertEquals(false, categoryRepository.existsById(id));

        Assertions.assertEquals(String.format(CategoryService.ID_NICHT_GEFUNDEN, id), categoryResponseEntity.getBody());
        verify(this.categoryRepository, times(0)).findById(any(Long.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }


    // findById
    // Positive Tests

    @Test
    void findById() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        Category category = new Category("TitleMuster", null,
                null, null);
        Optional optional = Optional.of(category);
        when(categoryRepository.findById(id)).thenReturn(optional);
        ResponseEntity<Category> categoryResponseEntity = this.categoryService.findById(id);
        Assertions.assertEquals(true, categoryRepository.existsById(id));
        Assertions.assertEquals(HttpStatus.OK, categoryResponseEntity.getStatusCode());
        Assertions.assertEquals(optional.get(), categoryResponseEntity.getBody());

        verify(this.categoryRepository, times(1)).findById(any(Long.class));
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findAll
    // Negative Tests
    @Test
    void findAll_list_empty() {
        List<Category> categories = new ArrayList<>();
        when(categoryRepository.findAll()).thenReturn(categories);
        ResponseEntity<List<Category>> all = this.categoryService.findAll();

        Assertions.assertEquals(CategoryService.GAR_KEINE_CATEGORY_VORHANDEN, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findAll
    // Positive Tests
    @Test
    void findAll() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        when(categoryRepository.findAll()).thenReturn(categories);
        ResponseEntity<List<Category>> all = this.categoryService.findAll();

        Assertions.assertEquals(categories, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findAll
    // Negative Tests
    @Test
    void findAllByLogin_login_null() {
        ResponseEntity<List<Category>> all = this.categoryService.findAllByLogin(null);
        Assertions.assertEquals(CategoryService.LOGIN_UNKORREKT, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findAllByLogin_login_leer() {
        ResponseEntity<List<Category>> all = this.categoryService.findAllByLogin("");
        Assertions.assertEquals(CategoryService.LOGIN_UNKORREKT, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findAllByLogin_liste_leer() {
        String login = "musterLoginAndrey_LoGin";
        List<Category> listeEmpty = new ArrayList<>();
        when(categoryRepository.findByEmployeesToCategoryLoginOrderByIdAsc(login)).thenReturn(listeEmpty);
        ResponseEntity<List<Category>> all = this.categoryService.findAllByLogin(login);
        Assertions.assertEquals(CategoryService.KEINE_CATEGORY_GEFUNDEN_LOGIN + login, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findAllByLogin_liste_null() {
        String login = "musterLoginAndrey_LoGin";
        List<Category> listNull = null;
        when(categoryRepository.findByEmployeesToCategoryLoginOrderByIdAsc(login)).thenReturn(listNull);
        ResponseEntity<List<Category>> all = this.categoryService.findAllByLogin(login);
        Assertions.assertEquals(CategoryService.KEINE_CATEGORY_GEFUNDEN_LOGIN + login, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findAll
    // Positive Tests
    @Test
    void findAllByLogin() {
        String login = "musterLoginAndrey_LoGin";
        List<Category> list = new ArrayList<>();
        list.add(new Category());
        when(categoryRepository.findByEmployeesToCategoryLoginOrderByIdAsc(login)).thenReturn(list);
        ResponseEntity<List<Category>> all = this.categoryService.findAllByLogin(login);
        Assertions.assertEquals(list, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findAllByEmailQuery
    // Negative Tests
    @Test
    void findAllByEmailQuery_email_null() {
        ResponseEntity<List<Category>> all = this.categoryService.findAllByEmailQuery("title", null);
        Assertions.assertEquals(CategoryService.EMAIL_UNKORREKT, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findAllByEmailQuery_email_leer() {
        ResponseEntity<List<Category>> all = this.categoryService.findAllByEmailQuery("title", "");
        Assertions.assertEquals(CategoryService.EMAIL_UNKORREKT, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findAllByEmailQuery_liste_leer() {
        String email = "musteremail@musteremail.de";
        List<Category> listeEmpty = new ArrayList<>();
        String title = "title";
        when(categoryRepository.findAllByEmailQuery(title, email)).thenReturn(listeEmpty);
        ResponseEntity<List<Category>> all = this.categoryService.findAllByEmailQuery(title, email);
        Assertions.assertEquals(CategoryService.KEINE_CATEGORY_GEFUNDEN_EMAIL + email + " . Title:" + title, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    @Test
    void findAllByEmailQuery_liste_null() {
        String email = "musteremail@musteremail.de";
        List<Category> listNull = null;
        String title = "title";
        when(categoryRepository.findAllByEmailQuery(title, email)).thenReturn(listNull);
        ResponseEntity<List<Category>> all = this.categoryService.findAllByEmailQuery(title, email);
        Assertions.assertEquals(CategoryService.KEINE_CATEGORY_GEFUNDEN_EMAIL + email + " . Title:" + title, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }

    // findAllByEmailQuery
    // Positive Tests
    @Test
    void findAllByEmailQuery() {
        String email = "musteremail@musteremail.de";
        List<Category> list = new ArrayList<>();
        list.add(new Category());
        when(categoryRepository.findAllByEmailQuery("title", email)).thenReturn(list);
        ResponseEntity<List<Category>> all = this.categoryService.findAllByEmailQuery("title", email);
        Assertions.assertEquals(list, all.getBody());
        org.assertj.core.api.Assertions.assertThatNoException();
    }
}
