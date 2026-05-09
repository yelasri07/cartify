import { Component, inject } from '@angular/core';
import { RouterLink } from "@angular/router";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthStateService } from '../../../../core/services/auth-state.service';

@Component({
  selector: 'app-register',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  private authStateService = inject(AuthStateService);

  registerForm = new FormGroup({
    name: new FormControl(""),
    email: new FormControl(""),
    password: new FormControl("")
  });

  onSubmit() {
    if (!this.name.value?.trim()) this.name.setErrors({ required: "User name cannot be empty" })
    if (!this.email.value?.trim()) this.email.setErrors({ required: "User email cannot be empty" })
    if (!this.password.value?.trim()) this.password.setErrors({ required: "User password cannot be empty" })

    if (this.registerForm.invalid) return;

    this.authStateService.register(this.registerForm.value).subscribe({
      next: res => {
        console.log(res)
      },
      error: err => {
        if (err.validationErrors) {
          
        }
      }
    })
  }

  get name() {
    return this.registerForm.controls.name
  }

  get email() {
    return this.registerForm.controls.email
  }

  get password() {
    return this.registerForm.controls.password
  }
}
