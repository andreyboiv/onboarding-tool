import {Routes} from '@angular/router';
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {InfoPageComponent} from "./auth/info-page/info-page.component";
import {ActivateAccountComponent} from "./auth/activate-account/activate-account.component";
import {SendEmailResetPassword} from "./auth/reset-password/send-email/send-email-reset-password.component";
import {UpdatePasswordComponent} from "./auth/reset-password/update-password/update-password.component";
import {MainComponent} from "./business/views/page/main/main.component";
import {Guard} from "./business/guard/roles-guard.service";

export const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'logout', redirectTo: '', pathMatch: 'full'},
  {path: 'index', redirectTo: '', pathMatch: 'full'},
  {path: 'register', component: RegisterComponent, pathMatch: 'full'},
  {path: 'info-page', component: InfoPageComponent, pathMatch: 'full'},
  {path: 'activate-account/:uuid', component: ActivateAccountComponent, pathMatch: 'full'},
  {path: 'reset-password', component: SendEmailResetPassword, pathMatch: 'full'},
  {path: 'update-password/:token', component: UpdatePasswordComponent, pathMatch: 'full'},

  {
    path: 'main', component: MainComponent, canActivate: [Guard]
  },

  {path: '**', redirectTo: '/'}, // Es werden alle anderen Anfragen auf die Hauptseite gelandet
];
