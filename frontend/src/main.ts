// src/main.ts
import 'zone.js'; // 👈 necesario para Angular
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app'; // <-- CORREGIDO

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
