import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, signal, computed } from '@angular/core';
import { Product } from '../interfaces/product.interface';
import { API } from '../config/api';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private http = inject(HttpClient);
  
  private readonly pageSize = 6;
  private readonly _page = signal(0);
  private readonly _products = signal<Product[]>([]);
  private readonly _loading = signal(false);

  // Exposed signals
  readonly page = computed(() => this._page());
  readonly products = computed(() => this._products());
  readonly loading = computed(() => this._loading());
  readonly isFirstPage = computed(() => this._page() === 0);
  readonly isLastPage = computed(() => this._products().length < this.pageSize);

  constructor() {
    this.loadProducts();
  }

  loadProducts() {
    this._loading.set(true);
    const params = new HttpParams()
      .set('page', this._page().toString())
      .set('size', this.pageSize.toString());

    this.http.get<Product[]>(API.GET_POSTS, { params }).subscribe({
      next: (products) => {
        this._products.set(products);
        this._loading.set(false);
      },
      error: (err) => {
        this._loading.set(false);
        throw err
      }
    });
  }

  nextPage() {
    if (!this.isLastPage()) {
      this._page.update(p => p + 1);
      this.loadProducts();
    }
  }

  previousPage() {
    if (!this.isFirstPage()) {
      this._page.update(p => Math.max(0, p - 1));
      this.loadProducts();
    }
  }
}
