package com.boivalenko.businessapp.teamtasksplanning.web.app.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskSearchValues {
    private String title;
    private Integer completed;
    private Long categoryId;
    private Long priorityId;
    private String email;

    // Für Intervall
    private Date dateFrom;
    private Date dateTo;

    // Für Paging
    private Integer pageNumber;
    private Integer pageSize;

    // Für Sortierung
    private String sortColumn;
    private String sortDirection;
}
