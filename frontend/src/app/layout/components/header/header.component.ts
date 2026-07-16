import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthStateService } from '../../../core/services/auth-state.service';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  private router = inject(Router)
  authStateService = inject(AuthStateService);
  user = this.authStateService.currentUser;
  isAuthenticated = this.authStateService.isAuthenticated;

  profileMenu = signal(false);

  logout() {
    this.authStateService.logout();
    this.router.navigateByUrl('/auth/login')
  }
}
