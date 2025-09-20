// src/app/app.component.ts

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './components/navbar/navbar'; // <-- CORREGIDO

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    Navbar
  ],
  templateUrl: './app.html', // <-- CORREGIDO
  styleUrl: './app.css'      // <-- CORREGIDO
})
export class App {
  title = 'ufinet-frontend';
}
