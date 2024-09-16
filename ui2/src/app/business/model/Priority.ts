import {Employee} from "../../auth/model/Employee";

export class Priority {
  id: number;
  title: string;
  color: string;
  user: Employee;

  constructor(id: number, title: string, color: string, user: Employee) {
    this.id = id;
    this.title = title;
    this.color = color;
    this.user = user;
  }
}
