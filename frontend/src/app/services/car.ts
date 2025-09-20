import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car } from '../models/car.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CarService {
  private apiUrl = `${environment.apiUrl}/cars`;

  constructor(private http: HttpClient) { }

  // Modificamos getCars para que acepte un objeto de filtros
  getCars(filters: any = {}): Observable<Car[]> {
    let params = new HttpParams();

    if (filters.search) {
      params = params.append('search', filters.search);
    }
    if (filters.brand) {
      params = params.append('brand', filters.brand);
    }
    if (filters.year) {
      params = params.append('year', filters.year);
    }

    return this.http.get<Car[]>(this.apiUrl, { params });
  }

  addCar(car: Car): Observable<Car> {
    return this.http.post<Car>(this.apiUrl, car);
  }

  updateCar(id: number, car: Car): Observable<Car> {
    return this.http.put<Car>(`${this.apiUrl}/${id}`, car);
  }

  deleteCar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
