import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {SpinnerService} from './spinner.service';

@Injectable()
// erwirscht alle HTTP Requests, um ein Spinner für Loading anzuzeigen
export class ShowSpinnerInterceptor implements HttpInterceptor {

  constructor(private spinnerService: SpinnerService) {
  }

  // erwirscht alle HTTP Requests
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    this.spinnerService.show(); // Spinner anzeigen

    return next
      .handle(req)
      .pipe(
        tap({ // Die Antwort ist eingetroffen – das bedeutet, dass die Anfrage abgeschlossen ist
          next: (event) => {
            if (event instanceof HttpResponse) {
              this.spinnerService.hide(); // Wenn die Anfrage abgeschlossen ist, wird der Spinner entfernt
            }
          },
          error: err => {
            console.log(err);
            this.spinnerService.hide(); // Wenn ein Fehler auftritt, wird der Spinner entfernt
          },
        }))
  }
}
