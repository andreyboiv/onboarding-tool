// Standard Methods von CRUD (create, read, update, delete)

import {Observable} from 'rxjs';

// alle Methods geben Observable zurück (für asynchrone und reaktive Funktionalität)
export interface ICommon<T> {

  findAll(email: string | undefined): Observable<T[]>;

  findById(id: number): Observable<T>;

  update(obj: T): Observable<T>;

  delete(id: number): Observable<T>;

  add(obj: T): Observable<T>;
}
