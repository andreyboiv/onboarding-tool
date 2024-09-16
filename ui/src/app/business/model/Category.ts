import {Employee} from "../../auth/model/Employee";

export class Category {
  id: number | any;
  title: string | any;
  completedCount: number | any;
  uncompletedCount: number | any;
  employeesToCategory: Employee | any;

  constructor(id: number | null, title: string, user: Employee | undefined, completedCount?: number, uncompletedCount?: number) {
    this.id = id;
    this.title = title;
    this.completedCount = completedCount;
    this.uncompletedCount = uncompletedCount;
    this.employeesToCategory = user;
  }
}
