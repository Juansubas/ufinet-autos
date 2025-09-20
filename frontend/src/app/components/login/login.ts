import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  loginForm: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.errorMessage = null; // Limpia errores anteriores
      this.authService.login(this.loginForm.value).subscribe({
        next: () => {
          this.router.navigate(['/cars']);
        },
        // --- AQUÍ ESTÁ LA CORRECCIÓN ---
        error: (err) => {
          // Revisa si el backend envió un mensaje específico
          if (err.error && err.error.message) {
            this.errorMessage = err.error.message;
          } else {
            // Si no, muestra un mensaje genérico
            this.errorMessage = 'Credenciales incorrectas o error en el servidor.';
          }
        }
      });
    }
  }
}
