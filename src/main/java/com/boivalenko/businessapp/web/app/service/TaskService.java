package com.boivalenko.businessapp.web.app.service;

import com.boivalenko.businessapp.web.app.entity.Task;
import com.boivalenko.businessapp.web.app.repository.TaskRepository;
import com.boivalenko.businessapp.web.app.search.TaskSearchValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskService implements IService<Task> {
    public static final String SORT_COLUMN_DEFAULT = "id";
    public static final String ZWEITES_SORTIERUNGS_FELD = "title";

    //Ab welcher Seite muss Search anfangen. 0 -> die erste Seite
    public static final Integer PAGE_NUMBER_DEFAULT_VALUE = 0;

    //Wie viel Elementen muss eine Seite enthalten
    public static final Integer PAGE_SIZE_DEFAULT_VALUE = 5;
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<Task> save(Task task) {
        if (task.getId() != null) {
            return new ResponseEntity("ID wird automatisch generiert. Man muss das nicht eingeben",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle().isEmpty() || task.getTitle().toLowerCase().contains("null")) {
            return new ResponseEntity("TITLE darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(this.taskRepository.save(task));
    }

    @Override
    public ResponseEntity<Task> update(Task task) {
        if (task.getId() == null || task.getId() == 0) {
            return new ResponseEntity("ID darf weder NULL noch 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle().isEmpty() || task.getTitle().toLowerCase().contains("null")) {
            return new ResponseEntity("TITLE darf weder NULL noch leer sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.taskRepository.existsById(task.getId()) == false) {
            return new ResponseEntity("ID=" + task.getId() + " nicht gefunden",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(this.taskRepository.save(task));
    }

    @Override
    public ResponseEntity<Task> deleteById(Long id) {
        if (id == 0) {
            return new ResponseEntity("ID darf nicht 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.taskRepository.existsById(id) == false) {
            return new ResponseEntity("ID=" + id + " nicht gefunden",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        this.taskRepository.deleteById(id);
        return new ResponseEntity("Task mit ID=" + id + " erfolgreich gelöscht", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Task> findById(Long id) {
        if (id == 0) {
            return new ResponseEntity("ID darf nicht 0 sein",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (this.taskRepository.existsById(id) == false) {
            return new ResponseEntity("ID=" + id + " nicht gefunden",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        Task task = this.taskRepository.findById(id).get();
        return ResponseEntity.ok(task);
    }

    @Override
    public ResponseEntity<List<Task>> findAll() {
        List<Task> all = this.taskRepository.findAll();
        if (all == null || all.isEmpty()) {
            return new ResponseEntity("gar kein Task vorhanden",
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<List<Task>> findAllByEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("EMAIL unkorrekt", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Task> allByEmail = this.taskRepository.findByEmployeesToTaskEmailOrderByTitleAsc(email);
        if (allByEmail == null || allByEmail.isEmpty()) {
            return new ResponseEntity("kein Task gefunden. Email:" + email,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(allByEmail);
    }

    public ResponseEntity<List<Task>> findAllByEmailQuery(String title, String email) {
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("EMAIL unkorrekt", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Task> allByEmailQuery = this.taskRepository.findAllByEmailQuery(title, email);
        if (allByEmailQuery == null || allByEmailQuery.isEmpty()) {
            return new ResponseEntity("kein Task gefunden. Email:" + email + " .Title:" + title,
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(allByEmailQuery);
    }

    public ResponseEntity<Page<Task>> findByParams(TaskSearchValues taskSearchValues) {
        String email = taskSearchValues.getEmail() != null ? taskSearchValues.getEmail() : null;

        // Email muss unbedingt vorhanden sein
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("EMAIL fehlt", HttpStatus.NOT_ACCEPTABLE);
        }

        String title = taskSearchValues.getTitle() != null && !taskSearchValues.getTitle().equals("") ? taskSearchValues.getTitle() : null;
        Boolean completed = taskSearchValues.getCompleted() != null && !taskSearchValues.getCompleted().equals("") && taskSearchValues.getCompleted() == 1 ? true : false;
        Long priorityId = taskSearchValues.getPriorityId() != null && !taskSearchValues.getPriorityId().equals("") ? taskSearchValues.getPriorityId() : null;
        Long categoryId = taskSearchValues.getCategoryId() != null && !taskSearchValues.getCategoryId().equals("") ? taskSearchValues.getCategoryId() : null;

        //Sort Column darf nicht NULL sein,
        // von daher wird es nach default gesetzt, falls keine SortColumn Parameter gegeben wird
        String sortColumn = taskSearchValues.getSortColumn() != null && !taskSearchValues.getSortColumn().equals("") ? taskSearchValues.getSortColumn() : SORT_COLUMN_DEFAULT;
        String sortDirection = taskSearchValues.getSortDirection() != null && !taskSearchValues.getSortDirection().equals("") ? taskSearchValues.getSortDirection() : null;

        //Ab welcher Seite muss Search anfangen. 0 -> die erste Seite
        Integer pageNumber = taskSearchValues.getPageNumber() != null && !taskSearchValues.getPageNumber().equals("") ? taskSearchValues.getPageNumber() : PAGE_NUMBER_DEFAULT_VALUE;

        //Wie viel Elementen muss eine Seite enthalten
        Integer pageSize = taskSearchValues.getPageSize() != null && !taskSearchValues.getPageSize().equals("") ? taskSearchValues.getPageSize() : PAGE_SIZE_DEFAULT_VALUE;

        Date dateFrom = null;
        Date dateTo = null;

        // DateFrom (Zeit - Stunden:Minuten:Sekunden:Millisekunden)
        // als 0:1:1:1 einsetzen,
        // damit es vom Anfang eines Tages beginnt
        if (taskSearchValues.getDateFrom() != null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskSearchValues.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 1);
            calendarFrom.set(Calendar.SECOND, 1);
            calendarFrom.set(Calendar.MILLISECOND, 1);
            dateFrom = calendarFrom.getTime();
        }

        // DateTo (Zeit - Stunden:Minuten:Sekunden:Millisekunden)
        // als 23:59:59:999 einsetzen,
        // damit es bis zum Ende eines Tages beendet
        if (taskSearchValues.getDateTo() != null) {
            Calendar calendarTo = Calendar.getInstance();
            calendarTo.setTime(taskSearchValues.getDateTo());
            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
            calendarTo.set(Calendar.MINUTE, 59);
            calendarTo.set(Calendar.SECOND, 59);
            calendarTo.set(Calendar.MILLISECOND, 999);
            dateTo = calendarTo.getTime();
        }

        // Sortierrichtung berechnen.
        // Wenn sortDirection == null oder
        // es gibt kein sortDirection Parameter
        // oder sortDirection.equalsIgnoreCase("asc") dann sortDirection = asc, ansonsten sortDirection = desc
        Sort.Direction direction = sortDirection == null ||
                sortDirection.trim().length() == 0 ||
                sortDirection.trim().equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;


        // ein Sortierobjekt, das eine Sortierungsspalte und eine Sortierungsrichtung enthält
        // ZWEITES_SORTIERUNGS_FELD  Parameter wird ein zweites Feld für Sortierung sein.
        // Z.b., wenn 2 Tasks den gleichen Prioritätswert haben und es wird nach diesem Feld sortiert.
        // Die Reihenfolge dieser 2 Datensätze nach Ausführung der Abfrage kann sich jedes Mal ändern,
        // weil das zweite Sortierfeld ist nicht angegeben.
        // Daher wird ZWEITES_SORTIERUNGS_FELD verwendet -
        // dann folgen alle Datensätze mit demselben Prioritätswert in derselben Reihenfolge nach ZWEITES_SORTIERUNGS_FELD
        Sort sort = Sort.by(direction, sortColumn, ZWEITES_SORTIERUNGS_FELD);

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // paginiertes Abfrageergebnis.
        // ein Objekt, das neben der Liste der
        // zurückgegebenen Elemente die Gesamtzahl der Seiten, Seitenzahl usw. enthält:
        Page<Task> result = this.taskRepository.findAllByParams(title,
                completed,
                priorityId,
                categoryId,
                email,
                dateFrom,
                dateTo,
                pageRequest);

        if (result.isEmpty()) {
            return new ResponseEntity("kein Task gefunden. Email:" + email,
                    HttpStatus.OK);
        }

        return ResponseEntity.ok(result);
    }
}
