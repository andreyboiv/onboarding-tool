package com.boivalenko.businessapp.teamtasksplanning.web.app.service;

import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Category;
import com.boivalenko.businessapp.teamtasksplanning.web.app.repository.CategoryRepository;
import com.boivalenko.businessapp.teamtasksplanning.web.base.IBaseService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService implements IBaseService<Category> {
    public static final String ID_NICHT_GEFUNDEN = "ID %d nicht gefunden";
    public static final String ID_WIRD_AUTOMATISCH_GENERIERT_MAN_MUSS_DAS_NICHT_EINGEBEN = "ID wird automatisch generiert. Man muss das nicht eingeben";
    public static final String NULL = "null";
    public static final String TITLE_DARF_WEDER_NULL_NOCH_LEER_SEIN = "TITLE darf weder NULL noch leer sein";
    public static final String ID_DARF_WEDER_NULL_NOCH_0_SEIN = "ID darf weder NULL noch 0 sein";
    public static final String CATEGORY_IST_ERFOLGREICH_UPDATED = "Category ist erfolgreich updated";
    public static final String ID_DARF_NICHT_0_SEIN = "ID darf nicht 0 sein";
    public static final String CATEGORY_MIT_ID = "Category mit ID=";
    public static final String ERFOLGREICH_GELOESCHT = " erfolgreich gelöscht";
    public static final String GAR_KEINE_CATEGORY_VORHANDEN = "gar keine Category vorhanden";
    public static final String EMAIL_UNKORREKT = "EMAIL unkorrekt";
    public static final String KEINE_CATEGORY_GEFUNDEN_EMAIL = "keine Category gefunden. Email:";
    public static final String LOGIN_UNKORREKT = "LOGIN unkorrekt";
    public static final String KEINE_CATEGORY_GEFUNDEN_LOGIN = "Keine Category gefunden. Login:";


    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity save(Category category) {
        if (category.getId() != null) {
            return new ResponseEntity<>(ID_WIRD_AUTOMATISCH_GENERIERT_MAN_MUSS_DAS_NICHT_EINGEBEN,
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle().isEmpty() || category.getTitle().toLowerCase().contains(NULL)) {
            return new ResponseEntity<>(TITLE_DARF_WEDER_NULL_NOCH_LEER_SEIN,
                    HttpStatus.NOT_ACCEPTABLE);
        }


        return new ResponseEntity<>(this.categoryRepository.save(category), HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(Category category) {
        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity<>(ID_DARF_WEDER_NULL_NOCH_0_SEIN,
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle().isEmpty() || category.getTitle().toLowerCase().contains(NULL)) {
            return new ResponseEntity<>(TITLE_DARF_WEDER_NULL_NOCH_LEER_SEIN,
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (categoryRepository.existsById(category.getId()) == false) {
            return new ResponseEntity<>(String.format(ID_NICHT_GEFUNDEN, category.getId()),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        this.categoryRepository.save(category);

        return new ResponseEntity<>(new Gson().toJson(CATEGORY_IST_ERFOLGREICH_UPDATED), HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        if (id == 0) {
            return new ResponseEntity<>(ID_DARF_NICHT_0_SEIN,
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.categoryRepository.existsById(id) == false) {
            return new ResponseEntity<>(String.format(ID_NICHT_GEFUNDEN, id),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        this.categoryRepository.deleteById(id);
        return new ResponseEntity<>(CATEGORY_MIT_ID + id + ERFOLGREICH_GELOESCHT, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Category> findById(Long id) {
        if (id == 0) {
            return new ResponseEntity(ID_DARF_NICHT_0_SEIN,
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.categoryRepository.existsById(id) == false) {
            return new ResponseEntity(String.format(ID_NICHT_GEFUNDEN, id),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Category> byId = this.categoryRepository.findById(id);

        return ResponseEntity.ok(byId.get());
    }

    @Override
    public ResponseEntity<List<Category>> findAll() {
        List<Category> all = this.categoryRepository.findAll();
        if (all.isEmpty()) {
            return new ResponseEntity(GAR_KEINE_CATEGORY_VORHANDEN,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<List<Category>> findAllByLogin(String login) {
        if (login == null || login.trim().length() == 0) {
            return new ResponseEntity(LOGIN_UNKORREKT, HttpStatus.NOT_ACCEPTABLE);
        }
        List<Category> allByLogin = this.categoryRepository.findByEmployeesToCategoryLoginOrderByIdDesc(login);
        if (allByLogin == null || allByLogin.isEmpty()) {
            return new ResponseEntity(KEINE_CATEGORY_GEFUNDEN_LOGIN + login,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(allByLogin);
    }

    public ResponseEntity<List<Category>> findAllByEmailQuery(String title, String email) {
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity(EMAIL_UNKORREKT, HttpStatus.NOT_ACCEPTABLE);
        }
        List<Category> allByEmailQuery = this.categoryRepository.findAllByEmailQuery(title, email);
        if (allByEmailQuery == null || allByEmailQuery.isEmpty()) {
            return new ResponseEntity(KEINE_CATEGORY_GEFUNDEN_EMAIL + email +" . Title:"+title,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(allByEmailQuery);
    }

}
