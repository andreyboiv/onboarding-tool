package com.boivalenko.businessapp.web.controller;

import com.boivalenko.businessapp.web.entity.Priority;
import com.boivalenko.businessapp.web.search.PrioritySearchValues;
import com.boivalenko.businessapp.web.service.PriorityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @PostMapping("/save")
    public ResponseEntity<Priority> save(@RequestBody Priority priority) {
        return this.priorityService.save(priority);
    }

    @PutMapping("/update")
    public ResponseEntity<Priority> update(@RequestBody Priority priority) {
        return this.priorityService.update(priority);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Priority> deleteById(@PathVariable("id") Long id) {
        return this.priorityService.deleteById(id);
    }

    @PostMapping("/findById")
    public ResponseEntity<Priority> findById(@RequestBody Long id) {
        return priorityService.findById(id);
    }

    @PostMapping("/findAll")
    public ResponseEntity<List<Priority>> findAll() {
        return this.priorityService.findAll();
    }

    @PostMapping("/findAllByEmail")
    public ResponseEntity<List<Priority>> findAllByEmail(@RequestBody String email) {
        return priorityService.findAllByEmail(email);
    }

    @PostMapping("/findAllByEmailQuery")
    public ResponseEntity<List<Priority>> findAllByEmailQuery(@RequestBody PrioritySearchValues prioritySearchValues) {
        return priorityService.findAllByEmailQuery(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());
    }

}
