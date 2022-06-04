package com.boivalenko.businessapp.web.service;

import com.boivalenko.businessapp.web.entity.Category;
import com.boivalenko.businessapp.web.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category findById(Long id){
        return repository.findById(id).get();
    }

}
