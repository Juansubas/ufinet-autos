// src/app/app.config.ts

import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './interceptors/auth-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    // Línea 1: Habilita la detección de cambios automática de Angular.
    // Gracias a esto, cuando una variable cambia, la vista se actualiza sola.
    provideZoneChangeDetection({ eventCoalescing: true }),
    // Línea 2: Activa el sistema de enrutamiento de Angular.
    // Le dice "usa las reglas definidas en el archivo 'routes'".
    provideRouter(routes),
    // Línea 3: Activa el cliente HTTP y le "conecta" tu interceptor.
    // 'provideHttpClient' hace que puedas pedir el servicio HttpClient en tus componentes.
    // 'withInterceptors' le dice que cada petición HTTP debe pasar primero por 'authInterceptor'.
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
