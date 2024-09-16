import {Inject, Injectable, InjectionToken} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from './CommonService';
import {ICategory} from '../interface/ICategory';
import {Category} from "../model/Category";

export const CATEGORY_URL_TOKEN = new InjectionToken<string>('url');

@Injectable({
  providedIn: 'root'
})

export class CategoryService extends CommonService<Category> implements ICategory {
  constructor(@Inject(CATEGORY_URL_TOKEN) private baseUrl: string, private http: HttpClient) {
    super(baseUrl, http);
  }
}
