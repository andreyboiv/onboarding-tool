package com.boivalenko.businessapp.teamtasksplanning.web.base;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBaseService<V> {
    ResponseEntity<V> save(V obj);
    ResponseEntity<V> update(V obj);
    ResponseEntity<V> deleteById(Long id);
    ResponseEntity<V> findById(Long id);
    ResponseEntity<List<V>> findAll();
}
