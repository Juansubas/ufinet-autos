import { ApplicationConfig } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './auth/auth.interceptor';

// Este archivo es la configuración central de tu aplicación.
export const appConfig: ApplicationConfig = {
  providers: [
    // Aquí registramos el HttpClient y le decimos que use nuestro
    // interceptor para añadir automáticamente el token a las peticiones.
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
