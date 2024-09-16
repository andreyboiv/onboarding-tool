import {Inject, Injectable, InjectionToken} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from './CommonService';
import {IPriority} from '../interface/IPriority';
import {Observable} from 'rxjs';
import {Priority} from "../model/Priority";

export const PRIORITY_URL_TOKEN = new InjectionToken<string>('url');

@Injectable({
  providedIn: 'root'
})

export class PriorityService extends CommonService<Priority> implements IPriority {
  constructor(@Inject(PRIORITY_URL_TOKEN) private baseUrl: string, private http: HttpClient) {
    super(baseUrl, http);
  }
}
