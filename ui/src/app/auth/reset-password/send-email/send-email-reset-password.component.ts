import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../../service/authservice/auth.service";
import {PatternConstants} from "../../../constants/pattern.constants";

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './send-email-reset-password.component.html',
  styleUrl: '../../auth.component.scss'
})
export class SendEmailResetPassword implements OnInit {

  passwordResetForm!: FormGroup;
  responseMessage: string | undefined;
  error: string | undefined;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.passwordResetForm = this.formBuilder.group({
      "email": new FormControl("", [Validators.required, 	Validators.pattern(PatternConstants.EMAIL_PATTERN)])
    })
  }

  submitPasswordResetForm() {

    if (this.passwordResetForm.invalid) {
      return;
    }

    const email = this.getEmail()?.value;

    this.authService.sendEmailResetPassword(email).subscribe({
        next: (responseMessage) => {
          this.error = '';
          this.router.navigate(['/info-page', {msg: responseMessage}], {skipLocationChange: true})
        },
        error: (err) => {
          this.responseMessage = '';
          if (err.status == 0) {
            this.error = 'Der Server antwortet nicht. Probieren Sie später noch mal...';
          } else {
            this.error = err.error == null ? 'Die E-mail Adresse existiert im System nicht' : err.error;
            this.router.navigate(['/info-page', {msg: this.error}], {skipLocationChange: true})
          }
        }
      }
    );
  }

  public getEmail() {
    return this.passwordResetForm.get('email');
  }

  toIndex() {
    this.router.navigate([''], {skipLocationChange: true})
  }

  register() {
    this.router.navigate(['register'], {skipLocationChange: true})
  }
}
