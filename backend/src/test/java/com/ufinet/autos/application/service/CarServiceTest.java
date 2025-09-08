package com.ufinet.autos.application.service;

import com.ufinet.autos.application.port.out.CarRepositoryPort;
import com.ufinet.autos.domain.exception.ResourceNotFoundException;
import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.domain.service.CarValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock // Mockito creará un mock de esta dependencia
    private CarRepositoryPort carRepositoryPort;

    @Mock // Mockito creará un mock de esta otra dependencia
    private CarValidationService carValidationService;

    @InjectMocks // Mockito inyectará los mocks anteriores en esta instancia de CarService
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks y las inyecciones antes de cada prueba
        MockitoAnnotations.openMocks(this);

        // Creamos un coche base para usar en las pruebas
        car = new Car();
        car.setId(1L);
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setPlate("ABC-123");
    }

    @Test
    void shouldGetCarByIdSuccessfully() {
        // Simula que el repositorio encuentra el coche
        when(carRepositoryPort.findById(1L)).thenReturn(Optional.of(car));

        Car result = carService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(carRepositoryPort, times(1)).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGettingNonExistentCar() {
        // Simula que el repositorio NO encuentra el coche
        when(carRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Verifica que se lance la excepción correcta
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> carService.getById(99L)
        );

        assertEquals("No se encontró el coche con el ID: 99", exception.getMessage());
    }

    @Test
    void shouldCreateCarSuccessfully() {
        // Simula la acción de guardado en el repositorio
        when(carRepositoryPort.save(any(Car.class))).thenReturn(car);

        // No necesitamos simular carValidationService.validate(),
        // porque por defecto un mock no hace nada (comportamiento void).

        Car createdCar = carService.create(car);

        assertNotNull(createdCar);
        // Verificamos que el método de validación fue llamado una vez
        verify(carValidationService, times(1)).validate(car);
        // Verificamos que el método de guardado fue llamado una vez
        verify(carRepositoryPort, times(1)).save(car);
    }


    @Test
    void shouldDeleteCarSuccessfully() {
        // 1. Simula que el coche SÍ se encuentra antes de borrarlo
        when(carRepositoryPort.findById(1L)).thenReturn(Optional.of(car));

        // 2. Simula la acción de borrado (no hace nada)
        doNothing().when(carRepositoryPort).deleteById(1L);

        // Ejecuta el método a probar
        assertDoesNotThrow(() -> carService.delete(1L));

        // 3. Verifica que deleteById fue llamado exactamente una vez
        verify(carRepositoryPort, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCar() {
        // Simula que el coche NO se encuentra
        when(carRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Verifica que se lance la excepción correcta
        assertThrows(ResourceNotFoundException.class, () -> {
            carService.delete(99L);
        });

        // Verifica que deleteById NUNCA fue llamado si el coche no se encontró
        verify(carRepositoryPort, never()).deleteById(anyLong());
    }
}
