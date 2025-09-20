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
  filterForm: FormGroup;
  editingCar: Car | null = null;
  isLoading = false;
  // --- AÑADE ESTAS PROPIEDADES PARA LOS MENSAJES ---
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private carService: CarService, private fb: FormBuilder) {
    this.carForm = this.fb.group({
      brand: ['', Validators.required],
      model: ['', Validators.required],
      year: ['', [Validators.required, Validators.min(1900), Validators.max(2030)]],
      plate: ['', Validators.required],
      color: ['', Validators.required],
      photo: [null]
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
        // Muestra un error si falla la carga de autos
        this.errorMessage = 'No se pudieron cargar los autos. Intenta de nuevo más tarde.';
      }
    });
  }

  applyFilters(): void {
    this.loadCars();
  }

  onSubmit(): void {
    if (this.carForm.invalid) return;

    this.isLoading = true;
    // Limpia los mensajes antes de cada operación
    this.errorMessage = null;
    this.successMessage = null;

    const operation = this.editingCar
      ? this.carService.updateCar(this.editingCar.id!, this.carForm.value)
      : this.carService.addCar(this.carForm.value);

    operation.subscribe({
      next: () => {
        this.successMessage = `Auto ${this.editingCar ? 'actualizado' : 'creado'} con éxito.`;
        this.filterForm.reset({ search: '', brand: '', year: '' });
        this.loadCars();
        this.cancelEdit();
        this.isLoading = false;
      },
      // --- ESTE BLOQUE ES LA MEJORA PRINCIPAL ---
      error: (err) => {
        if (err.error && err.error.message) {
          this.errorMessage = err.error.message; // Muestra el mensaje del backend
        } else {
          this.errorMessage = 'Ocurrió un error en la operación.';
        }
        console.error('Error en la operación', err);
        this.isLoading = false;
      }
    });
  }

  editCar(car: Car): void {
    this.successMessage = null; // Limpia mensajes al empezar a editar
    this.errorMessage = null;
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
          this.successMessage = 'Auto eliminado con éxito.';
          this.loadCars();
          this.isLoading = false;
        },
        error: (err) => {
          if (err.error && err.error.message) {
            this.errorMessage = err.error.message;
          } else {
            this.errorMessage = 'No se pudo eliminar el auto.';
          }
          console.error('Error al eliminar el auto', err);
          this.isLoading = false;
        }
      });
    }
  }
}
