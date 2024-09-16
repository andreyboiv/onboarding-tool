import {Component, inject, OnInit, ViewChild} from "@angular/core";
import {Category} from "../../../model/Category";
import {CategoryService} from "../../../services/CategoryService";
import {AuthService} from "../../../../auth/service/authservice/auth.service";
import {Employee} from "../../../../auth/model/Employee";
import {CategoriesComponent} from "../categories/categories.component";
import {CommonModule} from "@angular/common";
import {ActivatedRoute, Router, RouterOutlet} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatListModule} from "@angular/material/list";
import {MatSidenav, MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import {BreakpointObserver} from "@angular/cdk/layout";
import {MatTooltip} from "@angular/material/tooltip";
import {TaskService} from "../../../services/TaskService";
import {Task} from "../../../model/Task";
import {TasksComponent} from "../tasks/tasks.component";
import {SpinnerService} from "../../../spinner/spinner.service";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatDialog} from "@angular/material/dialog";
import {CategoryDoneDialogComponent} from "./all-category-done-dialog/category-done-dialog.component";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  standalone: true,
  imports: [
    CategoriesComponent,
    CommonModule,
    RouterOutlet,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatTooltip,
    TasksComponent,
    MatProgressSpinner
  ],
  styleUrls: ['./main.component.css']
})

export class MainComponent implements OnInit {

  categories: Category[] | any;
  tasks: Task[] | any;
  employee: Employee | any;
  readonly dialog = inject(MatDialog);

  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;
  isMobile = true;
  selectedCategory: Category | any;

  private category: Category = new Category(0, "0", new Employee(), 0, 0);
  anzahlAllUncompletedCountAllCat: number| any;

  spinner: SpinnerService | any;

  constructor(private authService: AuthService,
              private categoryService: CategoryService,
              private taskService: TaskService,
              private observer: BreakpointObserver,
              private spinnerService: SpinnerService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {

    this.spinner = this.spinnerService;

    this.observer.observe(['(max-width: 800px)']).subscribe((screenSize) => {
      if (screenSize.matches) {
        this.isMobile = true;
      } else {
        this.isMobile = false;
      }
    });

    this.authService.currentUser.subscribe(
      employee => {

        this.categoryService.findAll(employee?.login).subscribe(
          response => {
            this.employee = employee;
            this.categories = response;
            this.showAllTasksOfCategoryEvent(this.category);
            this.anzahlAllUncompletedCountAllCatBerechnen();
          }
        )
      }
    );
  }


  anzahlAllUncompletedCountAllCatBerechnen(){
    let count = 0;
    for (let categoriesKey of this.categories) {
      count += categoriesKey.uncompletedCount;
    }
    this.anzahlAllUncompletedCountAllCat = count;
    this.alleTaskErledigtValidate();
  }

  toggleMenu() {
    if (this.isMobile) {
      this.sidenav.toggle();
    } else {
      this.sidenav.open();
    }
  }

  showTasksOfCategoryEvent(category: Category) {
    if (category.id != 0) {
      this.taskService.findAllByCategoryID(category.id).subscribe(
        response => {
          this.selectedCategory = category;
          this.tasks = response;
        }
      )
    } else {
      this.showAllTasksOfCategoryEvent(category);
    }
  }

  showAllTasksOfCategoryEvent(category: Category) {
    this.taskService.findAll(this.employee?.login).subscribe(
      response => {
        this.selectedCategory = null;
        this.tasks = response;
      }
    )
  }

  updateTask(task: Task) {
    this.taskService.update(task).subscribe(result => {
      if (this.selectedCategory) {
        this.updateCategoryStatForKnownCategory(this.selectedCategory);
      } else {
        this.updateCategoryStatForNotKnownCategory(result);
      }
    });
  }

  private updateCategoryStatForKnownCategory(category: Category) {
    this.categoryService.findById(category.id).subscribe(categoryResponse => {
      this.selectedCategory.uncompletedCount = categoryResponse.uncompletedCount;
      this.anzahlAllUncompletedCountAllCatBerechnen();
    });
  }

  private updateCategoryStatForNotKnownCategory(result: Task) {
    this.taskService.findCategoryByTaskId(result).subscribe(categoryResponse => {
      for (let i = 0; i < this.categories.length; i++) {
        if (this.categories[i].id == categoryResponse.id) {
          this.categories[i].uncompletedCount = categoryResponse.uncompletedCount;
          break;
        }
      }
      this.anzahlAllUncompletedCountAllCatBerechnen();
    })
  }

  private alleTaskErledigtValidate() {
    if (this.anzahlAllUncompletedCountAllCat === 0) {

      const dialogRef =
        this.dialog.open(CategoryDoneDialogComponent, {
          disableClose: true
        });


      dialogRef.afterClosed().subscribe(result => {
        if (result === true) {
          this.deaktivateAccount();
        }
      });

    }
  }

  private deaktivateAccount() {
    this.route.params.subscribe(params => {
        this.authService.deActivateAccount(this.employee?.id).subscribe({
            next: (result) => {
              this.router.navigate(['/info-page', {
                msg: 'Dein Account ist deaktiviert. ' +
                  'Du kannst dich nicht wieder anmelden'
              }], {skipLocationChange: true});
            },
            error: (err) => {
            }
          }
        )
      }
    )
  }


}
