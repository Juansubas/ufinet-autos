package com.ufinet.autos.application.service;

import com.ufinet.autos.application.port.in.CarUseCase;
import com.ufinet.autos.application.port.out.CarRepositoryPort;
import com.ufinet.autos.domain.exception.ResourceNotFoundException;
import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.domain.service.CarValidationService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarService implements CarUseCase {

    private final CarRepositoryPort carRepositoryPort;
    private final CarValidationService carValidationService;

    public CarService(CarRepositoryPort carRepositoryPort, CarValidationService carValidationService) {
        this.carRepositoryPort = carRepositoryPort;
        this.carValidationService = carValidationService;
    }

    @Override
    public List<Car> listByUserWithFilters(Long userId, String search, String brand, Integer year) {
        return carRepositoryPort.findByCriteria(userId, search, brand, year);
    }

    @Override
    public List<Car> listByUser(Long userId) {
        return carRepositoryPort.findByUserId(userId);
    }

    @Override
    public Car getById(Long id) {
        return carRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el coche con el ID: " + id));
    }

    @Override
    public Car create(Car car) {
        // 1. Validar reglas de dominio (formato, año, etc.)
        carValidationService.validate(car);

        // 2. Validar reglas de caso de uso (unicidad)
        carRepositoryPort.findByPlate(car.getPlate()).ifPresent(existingCar -> {
            throw new IllegalArgumentException("Ya existe un coche con la matrícula: " + car.getPlate());
        });

        // 3. Persistir si todo es válido
        return carRepositoryPort.save(car);
    }

    @Override
    public Car update(Long id, Car car) {
        // 1. Validar reglas de dominio de los datos entrantes
        carValidationService.validate(car);

        // 2. Obtener la entidad existente
        Car existing = carRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el coche con el ID: " + id));

        // 3. Validar que la nueva matrícula no esté en uso por OTRO coche
        carRepositoryPort.findByPlate(car.getPlate()).ifPresent(otherCar -> {
            if (!otherCar.getId().equals(id)) {
                throw new IllegalArgumentException("La matrícula " + car.getPlate() + " ya está asignada a otro coche.");
            }
        });

        // 4. Actualizar y persistir
        existing.setBrand(car.getBrand());
        existing.setModel(car.getModel());
        existing.setYear(car.getYear());
        existing.setPlate(car.getPlate());
        existing.setColor(car.getColor());
        return carRepositoryPort.save(existing);
    }

    @Override
    public void delete(Long id) {
        // Primero, verificamos que el coche exista.
        // orElseThrow lanzará la excepción si el Optional está vacío.
        carRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se pudo eliminar. No se encontró el coche con el ID: " + id));

        // Si se encuentra, procedemos a borrarlo.
        carRepositoryPort.deleteById(id);
    }
}


