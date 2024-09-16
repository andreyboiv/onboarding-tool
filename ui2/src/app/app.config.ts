import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {HTTP_INTERCEPTORS, HttpClientModule, provideHttpClient} from "@angular/common/http";
import {RequestInterceptor} from "./auth/interceptor/request-interceptor.service";
import {TASK_URL_TOKEN} from "./business/services/TaskService";
import {CATEGORY_URL_TOKEN} from "./business/services/CategoryService";
import {PRIORITY_URL_TOKEN} from "./business/services/PriorityService";
import {STAT_URL_TOKEN} from "./business/services/StatService";
import {environment} from "../environments/environments";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ShowSpinnerInterceptor} from "./business/spinner/http-interceptor";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    importProvidersFrom(HttpClientModule),

    {provide: MAT_DIALOG_DATA, useValue: {} },
    {provide: MatDialogRef, useValue: {} },

    {provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi: true},

    {provide: HTTP_INTERCEPTORS, useClass: ShowSpinnerInterceptor, multi: true},

    {
      provide: TASK_URL_TOKEN,
      useValue: environment.backendURL + '/task'
    },

    {
      provide: CATEGORY_URL_TOKEN,
      useValue: environment.backendURL + '/category'
    },


    {
      provide: PRIORITY_URL_TOKEN,
      useValue: environment.backendURL + '/priority'
    },


    {
      provide: STAT_URL_TOKEN,
      useValue: environment.backendURL + '/stat'
    }, provideAnimationsAsync()
  ]
};
