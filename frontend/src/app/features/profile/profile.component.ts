import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthStateService } from '../../core/services/auth-state.service';
import { User } from '../../core/interfaces/user.interface';
import { ProductListComponent } from "../home/components/product-list/product-list.component";
import { PaginatorComponent } from "../home/components/paginator/paginator.component";
import { CreateProductComponent } from "../../shared/components/create-product/create-product.component";
import { PopupService } from '../../core/services/popup.service';
import { MediaService } from '../../core/services/media.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-profile',
  imports: [ProductListComponent, PaginatorComponent, CreateProductComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  private activatedRoute = inject(ActivatedRoute)
  private popupService = inject(PopupService)
  private mediaService = inject(MediaService)

  authStateService = inject(AuthStateService)

  userProfile = signal<User | null>(null);
  profileError = signal("");
  isCreateProductVisible = signal(false)
  isProfileImageUpdating = signal(false)

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(params => {
      this.userProfile.set(null)
      const userId = params.get('id')
      if (!userId) {
        this.profileError.set("Whoops! profile not found.")
        return
      }

      this.authStateService.fetchUserProfile(userId).subscribe({
        next: res => {
          this.profileError.set("")
          this.userProfile.set(res.user_details)
        },

        error: err => {
          if (err.status === 404) {
            this.profileError.set(err.error.message)
            return
          }

          throw err
        }
      })
    })
  }

  onProfileImageChange(event: Event) {
    const ipt = event.target as HTMLInputElement
    if (!ipt.files || ipt.files.length == 0) return;

    const file = ipt.files[0]
    ipt.value = ''

    const MAX_SIZE = 2 * 1024 * 1024;
    if (file.size > MAX_SIZE) {
      this.popupService.showError("Image size must not exceed 2MB.")
      return;
    }

    this.isProfileImageUpdating.set(true)
    this.mediaService.submitMedia([file], 'USER', this.authStateService.currentUser()?.id || '').pipe(
      finalize(() => {
        this.isProfileImageUpdating.set(false)
      })
    ).subscribe(res => {
      this.userProfile.update(p => ({ ...p!, avatar: res.files[0] }))
    })
  }

}
