package com.boivalenko.businessapp.onboarding.web.app.controller;

import com.boivalenko.businessapp.onboarding.web.app.entity.Priority;
import com.boivalenko.businessapp.onboarding.web.app.search.PrioritySearchValues;
import com.boivalenko.businessapp.onboarding.web.app.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priority")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @PostMapping("/add")
    public ResponseEntity<String> save(@RequestBody Priority priority) {
        return this.priorityService.save(priority);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        return this.priorityService.deleteById(id);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long id) {
        return this.priorityService.findById(id);
    }

    @PostMapping("/all")
    public ResponseEntity<List<Priority>> findAllByEmail(@RequestBody String email) {
        return this.priorityService.findAllByEmail(email);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Priority priority) {
        return this.priorityService.update(priority);
    }

    @PostMapping("/findAllByEmailQuery")
    public ResponseEntity<List<Priority>> findAllByEmailQuery(@RequestBody PrioritySearchValues prioritySearchValues) {
        return this.priorityService.findAllByEmailQuery(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());
    }
}
