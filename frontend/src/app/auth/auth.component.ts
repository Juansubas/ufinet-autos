import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html', // Usaremos un archivo HTML separado
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  // Lógica del componente de autenticación (código en el siguiente bloque)
}
