import {Component, OnInit} from '@angular/core';
import {NgIf} from "@angular/common";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../service/authservice/auth.service";
import {PatternConstants} from "../../constants/pattern.constants";
import {Employee} from "../model/Employee";


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './register.component.html',
  styleUrl: '../auth.component.scss'
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup;
  responseMessage: string | undefined;
  error: string | undefined;
  captchaText: string | undefined = '';

  private passwordMatchValidator: ValidatorFn | null = (
    control: AbstractControl
  ): ValidationErrors | null => {
    return control.value.password === control.value.confirm_password
      ? null
      : { PasswordNoMatch: true };
  };

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.generateCaptcha();

    this.registerForm = new FormGroup({
      "login": new FormControl("", [Validators.required, Validators.minLength(6), Validators.pattern('[a-zA-Z ]*')]),
      "email": new FormControl("", [Validators.required, Validators.pattern(PatternConstants.EMAIL_PATTERN)]),
      "password": new FormControl("", [Validators.required, Validators.minLength(8)]),
      "confirm_password": new FormControl("", [Validators.required, Validators.minLength(8)]),
      "confirm_captcha": new FormControl("", [Validators.required]),
    }, this.passwordMatchValidator)
  }

  submitRegisterForm() {

    if (this.registerForm.invalid) {
      return;
    }

    const tmpUser = new Employee();
    tmpUser.login = this.getLogin()?.value;
    tmpUser.email = this.getEmail()?.value;
    tmpUser.password = this.getPassword()?.value;
    this.authService.register(tmpUser).subscribe({
        next: (responseMessage) => {
          this.error = '',
            this.router.navigate(['/info-page', {
              msg: responseMessage}], {skipLocationChange: true})
        },
        error: (err) => {
          this.responseMessage = '';
          if (err.status == 0) {
            this.error = 'Der Server antwortet nicht. Probieren Sie später noch mal...';
          } else {
            this.error = err.error == null ? 'Anmeldedaten sind ungültig' : err.error;
          }
          if (this.registerForm.valid) {
            this.registerForm.reset();
          }
        }
      }
    );
  }

  public getLogin() {
    return this.registerForm.get('login');
  }

  public getEmail() {
    return this.registerForm.get('email');
  }

  public getPassword() {
    return this.registerForm.get('password');
  }

  public getConfirmPassword() {
    return this.registerForm.get('confirm_password');
  }

  public getConfirmCaptcha() {
    return this.registerForm.get('confirm_captcha');
  }

  generateCaptcha(): void {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZadcdefghijklmnopqrstuvwxyz0123456789';
    const captchaLength = 4;
    let captcha = '';
    for (let i = 0; i < captchaLength; i++) {
      const index = Math.floor(Math.random() * chars.length);
      captcha += chars[index];
    }
    this.captchaText = captcha;
  }

  einloggen() {
    this.router.navigate([''], {skipLocationChange: true})
  }
}
