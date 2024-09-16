import {Role} from "./Role";

export class Employee {
  id: number | undefined;
  login: string | undefined;
  password: string | undefined;
  email: string | undefined;
  powers: Array<Role> | undefined; // USER, ADMIN
}
