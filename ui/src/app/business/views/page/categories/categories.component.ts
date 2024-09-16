import {Component, EventEmitter, Inject, Input, Output} from '@angular/core';
import {Category} from "../../../model/Category";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {RouterOutlet} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {Employee} from "../../../../auth/model/Employee";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatTooltip} from "@angular/material/tooltip";
import {MatListItem} from "@angular/material/list";

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterOutlet,
    MatButton,
    MatIcon,
    MatFormField,
    MatInput,
    MatTooltip,
    MatLabel,
    MatListItem
  ],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css'
})
export class CategoriesComponent {

  @Output()
  showTasksOfCategoryEvent: EventEmitter<Category> = new EventEmitter<Category>();

  private allCategories: Category = new Category(0, "0", new Employee(), 0, 0);
  protected anzahlAllUncompletedCountAllCat: number | undefined;

  @Input('categories')
  set setCategories(categories: Category[]) {
    this.categories = categories;
  }

  @Input('anzahlAllUncompletedCountAllCat')
  set setAnzahlAllUncompletedCountAllCat(anzahlAllUncompletedCountAllCat: number) {
    this.anzahlAllUncompletedCountAllCat = anzahlAllUncompletedCountAllCat;
  }

  @Input('employeesToCategory')
  set setUser(employee: Employee) {
    this.employee = employee;
  }

  categories: Category[] | any;
  private employee: Employee | undefined;
  showEditIConCategoryIcon: boolean | undefined;
  indexCategoryMouseOver: number | undefined;
  selectedCategory: Category | undefined;
  allCategorien: boolean = true;

  updateEditIconVisible(show: boolean, index: number): void {
    this.showEditIConCategoryIcon = show;
    this.indexCategoryMouseOver = index;
  }

  showTasksOfCategory(category: Category) {
    if (this.selectedCategory === category) {
      return;
    }

    this.selectedCategory = category;

    this.showTasksOfCategoryEvent.emit(this.selectedCategory);
  }

  openAllTasksOfAllCategories() {
    this.allCategorien = true;
    this.showTasksOfCategory(this.allCategories);
  }
}
