// Este archivo centraliza las "formas" de nuestros datos para usarlas en toda la app.

export interface Car {
  id?: number;
  brand: string;
  model: string;
  year: number;
  plate: string;
  color: string;
  userId?: number;
}

export interface User {
  id?: number;
  username: string;
  email: string;
  password?: string;
}

export interface AuthResponse {
  token: string;
  message: string;
}
