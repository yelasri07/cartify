import { Component, EventEmitter, inject, OnInit, Output, signal } from '@angular/core';
import { ShoppingCartService } from '../../../core/services/shopping-cart.service';
import { CartItem } from '../../../core/interfaces/cart-item.interface';

@Component({
  selector: 'app-shopping-cart',
  imports: [],
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.scss',
})
export class ShoppingCartComponent implements OnInit {
  @Output()
  closeCart = new EventEmitter()

  private shoppingCartService = inject(ShoppingCartService)
  items = signal<CartItem[]>([]);

  ngOnInit(): void {
    this.shoppingCartService.fetchItems().subscribe(res => {
      this.items.set(res)
      console.log(this.items());
    })
  }

  updateItem(currentItem: CartItem, increment: boolean) {
    this.items.set(this.items().map(item => {
      if (currentItem.id != item.id) return item

      if (increment) {
        item.item_quantity++
      } else {
        item.item_quantity--
      }
      return item
    }))

    this.shoppingCartService.updateItem(currentItem.id, currentItem.product_id, currentItem.item_quantity).subscribe()
  }

  itemImage(item: CartItem) {
    const files = item.product.files;
    return files && files.length > 0
      ? files[0]
      : `https://placehold.co/600x400/222222/FFFFFF?text=${encodeURIComponent(item.product.name)}`;
  }
}
