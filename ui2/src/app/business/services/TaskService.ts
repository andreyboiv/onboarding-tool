import {Observable} from 'rxjs';
import {Inject, Injectable, InjectionToken} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {CommonService} from './CommonService';
import {ITask} from '../interface/ITask';
import {Task} from '../model/Task';
import {Category} from "../model/Category";

export const TASK_URL_TOKEN = new InjectionToken<string>('url');

@Injectable({
  providedIn: 'root'
})

export class TaskService extends CommonService<Task> implements ITask {

  constructor(@Inject(TASK_URL_TOKEN) private baseUrl: string, private http: HttpClient) {
    super(baseUrl, http);
  }


  findAllByCategoryID(id: number | undefined): Observable<Task[]> {
    return this.http.post<Task[]>(this.baseUrl + '/id', id);
  }

  findCategoryByTaskId(task: Task): Observable<Category> {
    return this.http.post<Category>(this.baseUrl + '/category', task);
  }
}
