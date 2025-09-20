import { Component, OnInit } from '@angular/core';
import { CarService } from '../../services/car';
import { Car } from '../../models/car.model';
import { NgClass } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-car-list',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgClass
  ],
  templateUrl: './car-list.html',
  styleUrl: './car-list.css'
})
export class CarList implements OnInit {
  cars: Car[] = [];
  carForm: FormGroup;
  filterForm: FormGroup; // <-- Formulario para filtros
  editingCar: Car | null = null;
  isLoading = false;

  constructor(private carService: CarService, private fb: FormBuilder) {
    this.carForm = this.fb.group({
      brand: ['', Validators.required],
      model: ['', Validators.required],
      year: ['', [Validators.required, Validators.min(1900), Validators.max(2030)]],
      plate: ['', Validators.required],
      color: ['', Validators.required],
      photo: [null] // <-- Campo de foto simulado
    });

    this.filterForm = this.fb.group({
      search: [''],
      brand: [''],
      year: ['']
    });
  }

  ngOnInit(): void {
    this.loadCars();
  }

  loadCars(): void {
    const filters = this.filterForm.value;
    this.isLoading = true;
    this.carService.getCars(filters).subscribe({
      next: (data) => {
        this.cars = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar los autos', err);
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    this.loadCars();
  }

  onSubmit(): void {
    if (this.carForm.invalid) return;

    this.isLoading = true;
    const operation = this.editingCar
      ? this.carService.updateCar(this.editingCar.id!, this.carForm.value)
      : this.carService.addCar(this.carForm.value);

    operation.subscribe({
      next: () => {
        this.filterForm.reset({ search: '', brand: '', year: '' });
        this.loadCars();
        this.cancelEdit();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error en la operación', err);
        this.isLoading = false;
      }
    });
  }

  editCar(car: Car): void {
    this.editingCar = car;
    this.carForm.patchValue(car);
  }

  cancelEdit(): void {
    this.editingCar = null;
    this.carForm.reset();
  }

  deleteCar(carId: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar este auto?')) {
      this.isLoading = true;
      this.carService.deleteCar(carId).subscribe({
        next: () => {
          this.loadCars();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error al eliminar el auto', err);
          this.isLoading = false;
        }
      });
    }
  }
}
