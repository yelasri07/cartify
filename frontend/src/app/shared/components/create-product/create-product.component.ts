import { Component, EventEmitter, inject, OnDestroy, OnInit, Output, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ProductService } from '../../../core/services/product.service';
import { MediaService } from '../../../core/services/media.service';
import { Confirmable } from '../../decorators/confirmable.decorator';
import { NgClass } from '@angular/common';
import { PopupService } from '../../../core/services/popup.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-create-product',
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './create-product.component.html',
  styleUrl: './create-product.component.scss'
})
export class CreateProductComponent implements OnInit, OnDestroy {
  private productService = inject(ProductService)
  private mediaService = inject(MediaService)
  private popupService = inject(PopupService)

  @Output()
  close = new EventEmitter()

  media = signal<{ url: string, file: File }[]>([])
  mediaError = signal<string>('')
  creatingProduct = signal<boolean>(false)

  productForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    price: new FormControl<any>(''),
    quantity: new FormControl('')
  })

  ngOnInit(): void {
    document.body.classList.add('overflow-hidden');
  }

  ngOnDestroy(): void {
    document.body.classList.remove('overflow-hidden');
  }

  onSubmit() {
    if (this.creatingProduct()) return;

    this.mediaError.set('')

    if (!this.name.value?.trim()) this.name.setErrors({ required: "Product name cannot be empty" })
    if (!this.description.value?.trim()) this.description.setErrors({ required: "Product description cannot be empty" })
    if (!this.price.value) this.price.setErrors({ required: "Product price cannot be empty" })
    if (!this.quantity.value) this.quantity.setErrors({ required: "Product quantity cannot be empty" })

    if (this.productForm.invalid) return;

    if (this.price.value && !isNaN(this.price.value)) {
      this.price.setValue(parseFloat(this.price.value).toFixed(2))
    }

    if (this.media().length === 0) {
      this.mediaError.set("Product images must be between 1 and 5 image")
      return
    };

    this.creatingProduct.set(true)
    this.productService.submitProduct(this.productForm.value).subscribe({
      next: res => {
        const files: File[] = this.media().map(m => m.file)
        this.mediaService.submitMedia(files, 'PRODUCT', res.id).pipe(
          finalize(() => {
            this.creatingProduct.set(false)
          })
        ).subscribe({
          next: () => {
            this.popupService.showSuccess("Product created successfully.")
            this.close.emit()
          }
        })
      },
      error: err => {
        this.creatingProduct.set(false)
        let fieldErrors = err.error?.validationErrors
        if (fieldErrors) {
          this.productForm.setErrors(fieldErrors)
          return;
        }

        throw err
      }
    })
  }

  onMediaChange(event: Event) {
    this.mediaError.set('')
    const ipt = event.target as HTMLInputElement

    if (this.media().length === 5) {
      ipt.value = ''
      this.popupService.showError("The product cannot exceed 5 images.")
      return;
    };

    if (!ipt.files || ipt.files?.length == 0) return;
    const file = ipt.files[0]
    ipt.value = ''

    const objectUrl = URL.createObjectURL(file)
    this.media().push({ url: objectUrl, file: file })
  }

  removeMedia(index: number) {
    URL.revokeObjectURL(this.media()[index].url)
    this.media().splice(index, 1)
  }

  closeModal() {
    let needConfirm = false
    Object.values(this.productForm.value).forEach(value => {
      if (value) {
        needConfirm = true
        return
      }
    })

    if (this.media().length > 0) {
      needConfirm = true
    }

    if (!needConfirm) {
      this.close.emit()
    } else {
      this.closeModalWithConfirmation()
    }
  }

  @Confirmable()
  private closeModalWithConfirmation() {
    this.close.emit()
  }

  get name() {
    return this.productForm.controls.name
  }

  get description() {
    return this.productForm.controls.description
  }

  get price() {
    return this.productForm.controls.price
  }

  get quantity() {
    return this.productForm.controls.quantity
  }

}
