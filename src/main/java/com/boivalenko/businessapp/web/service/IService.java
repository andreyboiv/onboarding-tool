package com.boivalenko.businessapp.web.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IService<V> {
    ResponseEntity<V> save(V obj);
    ResponseEntity<V> update(V obj);
    ResponseEntity<V> deleteById(Long id);
    ResponseEntity<V> findById(Long id);
    ResponseEntity<List<V>> findAll();
}
