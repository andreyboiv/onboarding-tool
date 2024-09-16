import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ICommon} from '../interface/ICommon';

export class CommonService<T> implements ICommon<T> {

  private readonly url: string;

  constructor(url: string, private httpClient: HttpClient) {
    this.url = url;
  }

  add(t: T): Observable<T> {
    return this.httpClient.post<T>(this.url + '/add', t);
  }

  delete(id: number | undefined): Observable<any> {
    return this.httpClient.delete<any>(this.url + '/delete/' + id);
  }

  findById(id: number | undefined): Observable<T> {
    return this.httpClient.post<T>(this.url + '/id', id);
  }

  findAll(login: string | undefined): Observable<T[]> {
    return this.httpClient.post<T[]>(this.url + '/all', login);
  }

  update(t: T): Observable<T> {
    return this.httpClient.patch<T>(this.url + '/update', t);
  }
}
