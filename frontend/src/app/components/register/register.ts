import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  registerForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.errorMessage = null; // Limpia errores anteriores
      this.successMessage = null;
      this.authService.register(this.registerForm.value).subscribe({
        next: () => {
          this.successMessage = '¡Registro exitoso! Serás redirigido al login.';
          setTimeout(() => this.router.navigate(['/login']), 2500);
        },
        // --- AQUÍ ESTÁ LA CORRECCIÓN ---
        error: (err) => {
          if (err.error && err.error.message) {
            this.errorMessage = err.error.message;
          } else {
            this.errorMessage = 'No se pudo completar el registro. Intenta con otros datos.';
          }
          this.successMessage = null;
        }
      });
    }
  }
}
