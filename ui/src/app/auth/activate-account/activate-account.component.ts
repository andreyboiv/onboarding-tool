import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {AuthService} from "../service/authservice/auth.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {PatternConstants} from "../../constants/pattern.constants";
import {CdkAccordion, CdkAccordionItem} from "@angular/cdk/accordion";

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, RouterLink, CdkAccordion, CdkAccordionItem],
  templateUrl: './activate-account.component.html',
  styleUrl: '../auth.component.scss'
})
export class ActivateAccountComponent implements OnInit {

  uuid: string | undefined;
  error: string | undefined;
  aktivateForm!: FormGroup;
  showForm: boolean | undefined;

  items = ['GANZ WICHTIGE INFO!!!'];
  expandedIndex = 0;

  constructor(private route: ActivatedRoute, private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.showForm = false;

    this.aktivateForm = new FormGroup({
      "email": new FormControl("", [Validators.required,
        Validators.pattern(PatternConstants.EMAIL_PATTERN)]),
    })

    this.route.params.subscribe(params => {
        this.uuid = params["uuid"];

        this.authService.activateAccount(this.uuid).subscribe({
            next: (result) => {
              this.router.navigate(['/info-page', {msg: 'Ihr Account ist erfolgreich aktiviert'}], {skipLocationChange: true});
            },
            error: (err) => {
              this.showForm = true;
             }
          }
        )
      }
    )
  }

  submitAktivateForm() {

    const email = this.getEmail()?.value;

    this.authService.resendActivateEmail(email).subscribe({
      next: (result) => {
        this.router.navigate(['/info-page', {msg: result}], {skipLocationChange: true})
      },
      error: (err) => {
        if (err.status == 0) {
          this.error = 'Der Server antwortet nicht. Probieren Sie später noch mal...';
        } else {
          this.error = err.error == null ? 'Die E-mail Adresse existiert im System nicht' : err.error;
        }
      }
    })
  }

  public getEmail() {
    return this.aktivateForm.get('email');
  }
}
