package com.boivalenko.businessapp.web.controller;

import com.boivalenko.businessapp.web.entity.Task;
import com.boivalenko.businessapp.web.search.TaskSearchValues;
import com.boivalenko.businessapp.web.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/save")
    public ResponseEntity<Task> save(@RequestBody Task task) {
        return this.taskService.save(task);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {
        return this.taskService.update(task);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Task> deleteById(@PathVariable("id") Long id) {
        return this.taskService.deleteById(id);
    }

    @PostMapping("/findById")
    public ResponseEntity<Task> findById(@RequestBody Long id) {
        return taskService.findById(id);
    }

    @PostMapping("/findAll")
    public ResponseEntity<List<Task>> findAll() {
        return this.taskService.findAll();
    }

    @PostMapping("/findAllByEmail")
    public ResponseEntity<List<Task>> findAllByEmail(@RequestBody String email) {
        return taskService.findAllByEmail(email);
    }

    @PostMapping("/findAllByEmailQuery")
    public ResponseEntity<List<Task>> findAllByEmailQuery(@RequestBody TaskSearchValues taskSearchValues) {
        return taskService.findAllByEmailQuery(taskSearchValues.getTitle(), taskSearchValues.getEmail());
    }

    // Suche nach beliebigen Parameter
    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues) throws ParseException {
        return this.taskService.findByParams(taskSearchValues);
    }

}
