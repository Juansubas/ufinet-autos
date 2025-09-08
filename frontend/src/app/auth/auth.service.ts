import { Injectable, signal, inject, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { User, AuthResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  // IMPORTANTE: URL de tu backend
  private readonly API_URL = 'http://localhost:8080/auth';

  // Signal para manejar el estado del token y del usuario de forma reactiva
  token = signal<string | null>(localStorage.getItem('authToken'));
  currentUser = computed(() => {
    const currentToken = this.token();
    if (!currentToken) return null;
    try {
      const payload = JSON.parse(atob(currentToken.split('.')[1]));
      return { username: payload.sub, id: payload.userId };
    } catch {
      this.logout();
      return null;
    }
  });

  register(user: User) {
    return this.http.post(`${this.API_URL}/register`, user);
  }

  login(credentials: { username: string; password: string }) {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => {
        this.token.set(response.token);
        localStorage.setItem('authToken', response.token);
      })
    );
  }

  logout() {
    this.token.set(null);
    localStorage.removeItem('authToken');
  }
}
