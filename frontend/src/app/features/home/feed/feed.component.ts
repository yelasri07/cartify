import { Component, inject } from '@angular/core';
import { ProductListComponent } from "../components/product-list/product-list.component";
import { PaginatorComponent } from "../components/paginator/paginator.component";
import { ProductService } from '../../../core/services/product.service';

@Component({
  selector: 'app-feed',
  imports: [ProductListComponent, PaginatorComponent],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.scss'
})
export class FeedComponent {
  private productService = inject(ProductService)

  search() {
    
  }
}
