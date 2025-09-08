import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Car } from '../models';

@Injectable({ providedIn: 'root' })
export class CarService {
  private http = inject(HttpClient);
  // IMPORTANTE: URL de tu backend
  private readonly API_URL = 'http://localhost:8080/cars';

  getCars() {
    return this.http.get<Car[]>(this.API_URL);
  }

  createCar(car: Car) {
    return this.http.post<Car>(this.API_URL, car);
  }

  updateCar(car: Car) {
    return this.http.put<Car>(`${this.API_URL}/${car.id}`, car);
  }

  deleteCar(id: number) {
    return this.http.delete(`${this.API_URL}/${id}`);
  }
}
