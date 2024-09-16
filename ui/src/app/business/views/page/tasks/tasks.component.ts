import {AfterViewInit, Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {MatLabel} from "@angular/material/form-field";
import { Task } from '../../../model/Task';
import {Category} from "../../../model/Category";
import {Employee} from "../../../../auth/model/Employee";
import {CommonModule} from "@angular/common";
import {
  MatCellDef, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource,
  MatTableModule
} from "@angular/material/table";
import {MatSort, MatSortModule} from "@angular/material/sort";
import {MatIcon} from "@angular/material/icon";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatPaginator} from "@angular/material/paginator";
import {CategoriesComponent} from "../categories/categories.component";

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [
    MatLabel,
    CommonModule,
    MatTable,
    MatTableModule,
    MatHeaderRow,
    MatRow,
    MatSort,
    MatTableModule,
    MatSortModule,
    MatHeaderRowDef,
    MatRowDef,
    MatCellDef,
    MatHeaderCellDef,
    MatIcon,
    MatCheckbox,
    MatPaginator,
    CategoriesComponent
  ],
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.css'
})
export class TasksComponent implements AfterViewInit {

  dataSource: MatTableDataSource<Task> = new MatTableDataSource<Task>();
  private tasks: Task[] | undefined;

  @ViewChild(MatPaginator) paginator: MatPaginator| any;
  @ViewChild(MatSort) sort: MatSort | any;

  @Output()
  updateTaskEvent = new EventEmitter<Task>();

  @Input('tasks')
  set setTasks(tasks: Task[]) {
    this.tasks = tasks;

    //Daten an die Tabelle übergeben, um Tasks anzuzeigen

    if (!this.dataSource) {
      return;
    }
    this.dataSource.data = this.tasks;
  }

  @Input() selectedCategory: Category | undefined;
  @Input() employee: Employee | undefined;
  displayedColumns: string[] = ['id', 'title', 'operations'];

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onToggleCompleted(task: Task) {
    task.completed = !task.completed;
    this.updateTaskEvent.emit(task);
  }
}
