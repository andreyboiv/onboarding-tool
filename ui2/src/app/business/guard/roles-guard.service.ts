import {inject, Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable, of} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {AuthService} from "../../auth/service/authservice/auth.service";

@Injectable({
  providedIn: 'root'
})
export class PermissionsService {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  /*
    Die Methode wird automatisch aufgerufen, wenn über einen mit einer Route verknüpften URI navigiert wird (sofern die angegebene Klasse für diese Route angegeben ist).
    Gibt true zurück – der Übergang wird ausgeführt, false – der Übergang ist verboten.
   */
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    // next - ein Parameter, der den Wert speichert – zu welcher URL wir gehen möchten

    if (this.authService.isLoggedIn) { // wenn der Benutzer bereits angemeldet ist
      return true;
    }

    // Wir versuchen, eine automatische Authentifizierung durchzuführen.
    // Wenn im Browser ein JWT-Cookie gespeichert wurde,
    // wird dieses an das Backend gesendet und der Benutzer meldet sich automatisch an

    // Wir senden eine Anfrage, um den Benutzer abzurufen
    // (da der Benutzer nicht lokal gespeichert ist, ist dies unsicher)
    // jedes Mal, wenn die Seite aktualisiert wird, müssen Sie den Benutzer erneut vom Backend abrufen
    return this.authService.autoLogin().pipe(
      map(result => {
        if (result) {

          const user = result;

          // Wir speichern den Benutzer im Service, damit wir ihn dann von dort abrufen können
          this.authService.currentUser.next(user);

          // Indikator dafür, dass sich der Benutzer erfolgreich angemeldet hat
          this.authService.isLoggedIn = true;

          return true;

        } else { // Wenn der Benutzer nicht autorisiert ist, wird er zur Hauptseite weitergeleitet.
          this.router.navigateByUrl('', {skipLocationChange: true});
          return false; // Navigieren zur angeforderten URL wird nicht ausgeführt
        }
      }),
      catchError((err) => {
        console.log(err);

        this.router.navigateByUrl('', {skipLocationChange: true});
        return of(false); // Navigieren zur angeforderten URL wird nicht ausgeführt
      })
    );
  }
}

export const Guard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  return <boolean>inject(PermissionsService).canActivate(next, state);
}
