package com.boivalenko.businessapp.teamtasksplanning.web.app.controller;

import com.boivalenko.businessapp.teamtasksplanning.web.app.search.CategorySearchValues;
import com.boivalenko.businessapp.teamtasksplanning.web.app.service.CategoryService;
import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Category category) {
        return this.categoryService.save(category);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Category category) {
        return this.categoryService.update(category);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        return this.categoryService.deleteById(id);
    }

    @PostMapping("/findById")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        return this.categoryService.findById(id);
    }

    @PostMapping("/findAll")
    public ResponseEntity<List<Category>> findAll() {
        return this.categoryService.findAll();
    }

    @PostMapping("/findAllByEmail")
    public ResponseEntity<List<Category>> findAllByEmail(@RequestBody String email) {
        return this.categoryService.findAllByEmail(email);
    }

    @PostMapping("/findAllByEmailQuery")
    public ResponseEntity<List<Category>> findAllByEmailQuery(@RequestBody CategorySearchValues categorySearchValues) {
        return this.categoryService.findAllByEmailQuery(categorySearchValues.getTitle(), categorySearchValues.getEmail());
    }

}
