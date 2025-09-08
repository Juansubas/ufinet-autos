import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from './auth/auth.service';
import { AuthComponent } from './auth/auth.component';
import { CarListComponent } from './cars/car-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, AuthComponent, CarListComponent],
  // Este es el "director de orquesta": muestra la vista de login o la de autos
  // dependiendo de si el usuario está autenticado o no.
  template: `
    @if (isLoggedIn()) {
      <app-car-list />
    } @else {
      <app-auth />
    }
  `,
})
export class AppComponent {
  private authService = inject(AuthService);
  // Un signal computado que reacciona a los cambios en el estado de autenticación.
  isLoggedIn = computed(() => !!this.authService.currentUser());
}
