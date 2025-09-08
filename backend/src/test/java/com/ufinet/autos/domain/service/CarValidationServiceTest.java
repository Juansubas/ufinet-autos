package com.ufinet.autos.domain.service;

import com.ufinet.autos.domain.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Year;
import static org.junit.jupiter.api.Assertions.*;

class CarValidationServiceTest {

    private final CarValidationService validationService = new CarValidationService();
    private Car car;

    // Se ejecuta antes de cada prueba para tener un objeto Car base
    @BeforeEach
    void setUp() {
        car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Rojo");
        car.setPlate("ABC-123");
        car.setYear(2022);
    }

    @Test
    void shouldPassWithValidCar() {
        // La prueba ahora pasa porque el objeto 'car' del setUp es completamente válido.
        assertDoesNotThrow(() -> validationService.validate(car));
    }

    @Test
    void shouldThrowExceptionIfBrandIsNull() {
        car.setBrand(null); // Caso nulo

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validationService.validate(car);
        });

        assertEquals("La marca del coche no puede estar vacía.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfModelIsEmpty() {
        car.setModel(""); // Caso vacío

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validationService.validate(car);
        });

        assertEquals("El modelo del coche no puede estar vacío.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidPlateFormat() {
        car.setPlate("ABC1234"); // Formato inválido (falta el guion)

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validationService.validate(car);
        });

        assertEquals("El formato de la matrícula no es válido. Debe ser como 'ABC-123'.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForFutureYear() {
        car.setYear(Year.now().getValue() + 1); // Año en el futuro

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validationService.validate(car);
        });

        assertEquals("El año del coche no puede ser superior al año actual.", exception.getMessage());
    }
}
