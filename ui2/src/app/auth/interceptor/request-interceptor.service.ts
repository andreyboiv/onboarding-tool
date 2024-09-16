import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";


//erwirscht alle Requests. Dabei können die Requests modifiziert werden ...
@Injectable({
  providedIn: 'root'
})
export class RequestInterceptor implements HttpInterceptor {

  //this method wird immer laufen wenn die Requests an Backend gehen
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    //wir erlauben Verschicken von Cookies an Backend
    request = request.clone({
      withCredentials: true
    })

    if (request.url.includes("update-password")) {

      const token = request.params.get('token');
      request.params.delete('token');

      request = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + token)
      });
    }

    return next.handle(request);
  }
}
