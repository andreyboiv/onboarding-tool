import {BehaviorSubject} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class SpinnerService {

  visibility = new BehaviorSubject(false);

  show(): void {
    this.visibility.next(true);
  }

  hide(): void {
    this.visibility.next(false);
  }
}
