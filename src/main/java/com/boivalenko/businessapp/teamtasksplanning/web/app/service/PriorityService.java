package com.boivalenko.businessapp.teamtasksplanning.web.app.service;

import com.boivalenko.businessapp.teamtasksplanning.web.app.repository.PriorityRepository;
import com.boivalenko.businessapp.teamtasksplanning.web.base.IBaseService;
import com.boivalenko.businessapp.teamtasksplanning.web.app.entity.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PriorityService implements IBaseService<Priority> {
    private final PriorityRepository priorityRepository;

    @Override
    public ResponseEntity<Priority> save(Priority priority) {
        if (priority.getId() != null) {
            return new ResponseEntity("ID wird automatisch generiert. Man muss das nicht eingeben",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle().isEmpty() || priority.getTitle().toLowerCase().contains("null")) {
            return new ResponseEntity("TITLE darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(this.priorityRepository.save(priority));
    }

    @Override
    public ResponseEntity<Priority> update(Priority priority) {
        if (priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity("ID darf weder NULL noch 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle().isEmpty() || priority.getTitle().toLowerCase().contains("null")) {
            return new ResponseEntity("TITLE darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.priorityRepository.existsById(priority.getId()) == false) {
            return new ResponseEntity("ID=" + priority.getId() + " nicht gefunden",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(this.priorityRepository.save(priority));
    }

    @Override
    public ResponseEntity<Priority> deleteById(Long id) {
        if (id == 0) {
            return new ResponseEntity("ID darf nicht 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.priorityRepository.existsById(id) == false) {
            return new ResponseEntity("ID=" + id + " nicht gefunden",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        this.priorityRepository.deleteById(id);
        return new ResponseEntity("Priority mit ID=" + id + " erfolgreich gelöscht", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Priority> findById(Long id) {
        if (id == 0) {
            return new ResponseEntity("ID darf nicht 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.priorityRepository.existsById(id) == false) {
            return new ResponseEntity("ID=" + id + " nicht gefunden",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        Priority priority = this.priorityRepository.findById(id).get();
        return ResponseEntity.ok(priority);
    }

    @Override
    public ResponseEntity<List<Priority>> findAll() {
        List<Priority> all = this.priorityRepository.findAll();
        if (all == null || all.isEmpty()) {
            return new ResponseEntity("keine Priority vorhanden",
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<List<Priority>> findAllByEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("EMAIL unkorrekt", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Priority> allByEmail = this.priorityRepository.findByEmployeesToPriorityEmailOrderByTitleAsc(email);
        if (allByEmail == null || allByEmail.isEmpty()) {
            return new ResponseEntity("keine Priority vorhanden. Email: " + email,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(allByEmail);
    }

    public ResponseEntity<List<Priority>> findAllByEmailQuery(String title, String email) {
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("EMAIL unkorrekt", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Priority> allByEmailQuery = this.priorityRepository.findAllByEmailQuery(title, email);
        if (allByEmailQuery == null || allByEmailQuery.isEmpty()) {
            return new ResponseEntity("keine Priority vorhanden. Email: " + email +" . Title: "+title,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(allByEmailQuery);
    }

}
