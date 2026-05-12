import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
// import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private http = inject(HttpClient);

  getProducts(page: string, size: string) {
    return this.http.post<User>(API.REGISTER, userData);
  }
}
