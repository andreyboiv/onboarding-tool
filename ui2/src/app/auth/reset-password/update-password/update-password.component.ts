import {Component, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {AuthService} from "../../service/authservice/auth.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-update-password',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './update-password.component.html',
  styleUrl: '../../auth.component.scss'
})
export class UpdatePasswordComponent implements OnInit {

  updatePasswordForm!: FormGroup;
  error: string | undefined;
  showPasswordForm = false;
  token: string | undefined;

  private passwordMathValidator: ValidatorFn | null = (
    control: AbstractControl
  ): ValidationErrors | null => {
    return control.value.password === control.value.confirm_password
      ? null
      : { PasswordNoMatch: true };
  };

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService) {
  }

  ngOnInit(): void {

    this.updatePasswordForm = new FormGroup({
      "password": new FormControl("", [Validators.required, Validators.minLength(8)]),
      "confirm_password": new FormControl("", [Validators.required, Validators.minLength(8)]),
    }, this.passwordMathValidator)


    this.route.params.subscribe(params => {
      this.token = params['token'];
        this.showPasswordForm = true;
    });

  }

  public getPassword() {
    return this.updatePasswordForm.get('password');
  }

  public getConfirmPassword() {
    return this.updatePasswordForm.get('confirmPassword');
  }

  public submitUpdatePasswordForm(): void {

    if (this.updatePasswordForm.invalid) {
      return;
    }

    this.authService.updatePassword(this.getPassword()?.value, this.token).subscribe({
        next: (responseMessage) => {

          if (responseMessage) {
            this.router.navigate(['/info-page', {msg: 'Passwort ist erfolgreich geändert'}], {skipLocationChange: true});
          }

        },
        error: (err) => {
          if (err.status == 0) {
            this.error = 'Der Server antwortet nicht. Probieren Sie später noch mal...';
          } else {
            this.router.navigate(['/info-page', {
              msg: 'Es ist ein Fehler bezüglich ' +
                'einer Passwort Änderung aufgetreten. ' +
                'Vielleicht ist die Dauer von der Token Gültigkeit abgelaufen. ' +
                'In dem Fall fordere bitte das Passwort noch mal'
            }], {skipLocationChange: true});
          }
        }
      }
    );
  }
}
