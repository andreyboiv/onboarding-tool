package com.boivalenko.businessapp.teamtasksplanning.web.app.controller;

import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Category;
import com.boivalenko.businessapp.teamtasksplanning.web.app.search.CategorySearchValues;
import com.boivalenko.businessapp.teamtasksplanning.web.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Category> save(@RequestBody Category category) {
        return this.categoryService.save(category);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        return this.categoryService.deleteById(id);
    }

    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        return this.categoryService.findById(id);
    }

    @PostMapping("/all")
    public ResponseEntity<List<Category>> findAll(@RequestBody String login) {
        return this.categoryService.findAllByLogin(login);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(@RequestBody Category category) {
        return this.categoryService.update(category);
    }

    @PostMapping("/findAllByEmailQuery")
    public ResponseEntity<List<Category>> findAllByEmailQuery(@RequestBody CategorySearchValues categorySearchValues) {
        return this.categoryService.findAllByEmailQuery(categorySearchValues.getTitle(), categorySearchValues.getEmail());
    }

}
