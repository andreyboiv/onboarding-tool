package com.boivalenko.businessapp.web.app.controller;

import com.boivalenko.businessapp.web.app.entity.Priority;
import com.boivalenko.businessapp.web.app.search.PrioritySearchValues;
import com.boivalenko.businessapp.web.app.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priority")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

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
        return this.priorityService.findById(id);
    }

    @PostMapping("/findAll")
    public ResponseEntity<List<Priority>> findAll() {
        return this.priorityService.findAll();
    }

    @PostMapping("/findAllByEmail")
    public ResponseEntity<List<Priority>> findAllByEmail(@RequestBody String email) {
        return this.priorityService.findAllByEmail(email);
    }

    @PostMapping("/findAllByEmailQuery")
    public ResponseEntity<List<Priority>> findAllByEmailQuery(@RequestBody PrioritySearchValues prioritySearchValues) {
        return this.priorityService.findAllByEmailQuery(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());
    }

}
