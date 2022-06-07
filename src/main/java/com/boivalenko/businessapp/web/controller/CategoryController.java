package com.boivalenko.businessapp.web.controller;

import com.boivalenko.businessapp.web.entity.Category;
import com.boivalenko.businessapp.web.search.CategorySearchValues;
import com.boivalenko.businessapp.web.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public ResponseEntity<Category> save(@RequestBody Category category) {
        return this.categoryService.save(category);
    }

    @PutMapping("/update")
    public ResponseEntity<Category> update(@RequestBody Category category) {
        return this.categoryService.update(category);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Category> deleteById(@PathVariable("id") Long id) {
        return this.categoryService.deleteById(id);
    }

    @PostMapping("/findById")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        return categoryService.findById(id);
    }

    @PostMapping("/findAll")
    public ResponseEntity<List<Category>> findAll() {
        return this.categoryService.findAll();
    }

    @PostMapping("/findAllByEmail")
    public ResponseEntity<List<Category>> findAllByEmail(@RequestBody String email) {
        return categoryService.findAllByEmail(email);
    }

    @PostMapping("/findAllByEmailQuery")
    public ResponseEntity<List<Category>> findAllByEmailQuery(@RequestBody CategorySearchValues categorySearchValues) {
        return categoryService.findAllByEmailQuery(categorySearchValues.getTitle(), categorySearchValues.getEmail());
    }

}
