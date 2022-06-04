package com.boivalenko.businessapp.web.controller;

import com.boivalenko.businessapp.web.entity.Category;
import com.boivalenko.businessapp.web.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public Category getID(@PathVariable(value="id") Long id){
        return categoryService.findById(id);
    }

}
