package com.boivalenko.businessapp.teamtasksplanning.web.app.service;

import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Category;
import com.boivalenko.businessapp.teamtasksplanning.web.app.repository.CategoryRepository;
import com.boivalenko.businessapp.teamtasksplanning.web.base.IBaseService;
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
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<Category> save(Category category) {
        if (category.getId() != null) {
            return new ResponseEntity("ID wird automatisch generiert. Man muss das nicht eingeben",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle().isEmpty() || category.getTitle().toLowerCase().contains("null")) {
            return new ResponseEntity("TITLE darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(this.categoryRepository.save(category));
    }

    @Override
    public ResponseEntity<Category> update(Category category) {
        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity("ID darf weder NULL noch 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle().isEmpty() || category.getTitle().toLowerCase().contains("null")) {
            return new ResponseEntity("TITLE darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (!this.categoryRepository.existsById(category.getId())) {
            return new ResponseEntity(String.format(ID_NICHT_GEFUNDEN, category.getId()),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(this.categoryRepository.save(category));
    }

    @Override
    public ResponseEntity<Category> deleteById(Long id) {
        if (id == 0) {
            return new ResponseEntity("ID darf nicht 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (!this.categoryRepository.existsById(id)) {
            return new ResponseEntity(String.format(ID_NICHT_GEFUNDEN, id),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        this.categoryRepository.deleteById(id);
        return new ResponseEntity("Category mit ID=" + id + " erfolgreich gelöscht", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Category> findById(Long id) {
        if (id == 0) {
            return new ResponseEntity("ID darf nicht 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (!this.categoryRepository.existsById(id)) {
            return new ResponseEntity(String.format(ID_NICHT_GEFUNDEN, id),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        Category category = null;

        Optional<Category> byId = this.categoryRepository.findById(id);
        if (byId.isPresent()) {
            category = byId.get();
        }

        return ResponseEntity.ok(category);
    }

    @Override
    public ResponseEntity<List<Category>> findAll() {
        List<Category> all = this.categoryRepository.findAll();
        if (all.isEmpty()) {
            return new ResponseEntity("gar keine Category vorhanden",
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<List<Category>> findAllByEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("EMAIL unkorrekt", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Category> allByEmail = this.categoryRepository.findByEmployeesToCategoryEmailOrderByTitleAsc(email);
        if (allByEmail == null || allByEmail.isEmpty()) {
            return new ResponseEntity("keine Category gefunden. Email:" + email,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(allByEmail);
    }

    public ResponseEntity<List<Category>> findAllByEmailQuery(String title, String email) {
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("EMAIL unkorrekt", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Category> allByEmailQuery = this.categoryRepository.findAllByEmailQuery(title, email);
        if (allByEmailQuery == null || allByEmailQuery.isEmpty()) {
            return new ResponseEntity("keine Category gefunden. Email:" + email +" . Title:"+title,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(allByEmailQuery);
    }

}
