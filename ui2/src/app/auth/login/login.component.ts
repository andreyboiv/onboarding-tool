import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../service/authservice/auth.service";
import {Router, RouterLink} from "@angular/router";
import {NgIf} from "@angular/common";
import {Employee} from "../model/Employee";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule, NgIf, RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: '../auth.component.scss'
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  responseMessage: string | undefined;
  error: string | undefined;
  private user: Employee | undefined;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      "login": new FormControl("", [Validators.required, Validators.minLength(6), Validators.pattern('[a-zA-Z ]*')]),
      "password": new FormControl("", [Validators.required, Validators.minLength(8)])
    })
  }


  submitAuthorisationForm() {

    if (this.loginForm.invalid) {
      return;
    }

    const tmpUser = new Employee();
    tmpUser.login = this.getLogin()?.value;
    tmpUser.password = this.getPassword()?.value;
    this.authService.login(tmpUser).subscribe({
        next: (responseMessage) => {
          this.error = '';
          tmpUser.password = '';
          tmpUser.id = Number(responseMessage);
          this.user = tmpUser;

          this.authService.currentUser.next(this.user);
          this.authService.isLoggedIn = true;

          this.router.navigate(['main'], {skipLocationChange: true})
        },
        error: (err) => {
          this.responseMessage = '';
          if (err.status == 0) {
            this.error = 'Der Server antwortet nicht. Probieren Sie später noch mal...';
          } else {
            this.error = err.error == null ? 'Anmeldedaten sind ungültig' : err.error;
          }
        }
      }
    );
  }

  public getLogin() {
    return this.loginForm.get('login');
  }

  public getPassword() {
    return this.loginForm.get('password');
  }

  register() {
    this.router.navigate(['register'], {skipLocationChange: true})
  }

  resetPassword() {
    this.router.navigate(['reset-password'], {skipLocationChange: true})
  }
}
