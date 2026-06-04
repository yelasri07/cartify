import {
  Component,
  input,
  computed,
  signal,
  inject,
  CUSTOM_ELEMENTS_SCHEMA,
} from '@angular/core';
import { Product } from '../../../../core/interfaces/product.interface';
import { CurrencyPipe } from '@angular/common';
import { ProductOptionsComponent } from '../../../../shared/components/product-options/product-options.component';
import { AuthStateService } from '../../../../core/services/auth-state.service';
import { CreateProductComponent } from '../../../../shared/components/create-product/create-product.component';
import '@tailwindplus/elements';

@Component({
  selector: 'app-product-card',
  imports: [CurrencyPipe, ProductOptionsComponent, CreateProductComponent],
  templateUrl: './product-card.component.html',
  styleUrl: './product-card.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ProductCardComponent {
  product = input.required<Product>();
  currentUser = inject(AuthStateService);
  selectedImageIndex = signal(0);

  currentDrawerImage = computed(
    () => this.drawerImages()[this.selectedImageIndex()],
  );

  drawerImages = computed(() => {
    const files = this.product().files || [];
    if (files.length === 0) {
      return [
        `https://placehold.co/600x400/222222/FFFFFF?text=${encodeURIComponent(this.product().name)}`,
      ];
    }
    return files.slice(0, 5); // Max 5 images
  });

  isVisibleOptions = signal<boolean>(false);
  isVisibleUpdateProduct = signal<boolean>(false);

  productImage = computed(() => {
    const files = this.product().files;
    return files && files.length > 0
      ? files[0]
      : `https://placehold.co/600x400/222222/FFFFFF?text=${encodeURIComponent(this.product().name)}`;
  });

  selectImage(index: number) {
    this.selectedImageIndex.set(index);
  }

  isLowStock = computed(
    () => this.product().quantity > 0 && this.product().quantity < 5,
  );
  isOutOfStock = computed(() => this.product().quantity === 0);
}
