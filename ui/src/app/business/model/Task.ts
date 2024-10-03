import {Category} from './Category';
import {Employee} from "../../auth/model/Employee";

export class Task {
  id: number;
  title: string;
  completed: boolean;
  category: Category;
  taskDate?: Date;
  user: Employee;
  oldCategory: Category | undefined;

  constructor(id: number, title: string, completed: boolean,
              category: Category, employee: Employee, taskDate?: Date) {
    this.id = id;
    this.title = title;
    this.completed = completed;
    this.category = category;
    this.taskDate = taskDate;
    this.user = employee;
  }
}
