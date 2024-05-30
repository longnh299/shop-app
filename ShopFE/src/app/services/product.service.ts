import { Injectable } from '@angular/core';
import { Product } from '../models/product';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  private apiGetProducts = `${environment.baseUrl}/products`;

  getProducts(keyword: string, selectedCategoryId: number, page: number, size: number): Observable<Product[]> {

    const params = new HttpParams()
    .set('keyword', keyword)
    .set('category_id', selectedCategoryId.toString())
    .set('page', page.toString())
    .set('size', size.toString()); 

    return this.http.get<Product[]>(this.apiGetProducts, { params });

  }

  getDetailProduct(productId: number) {
    return this.http.get(`${environment.baseUrl}/products/${productId}`);
  }
  
}
