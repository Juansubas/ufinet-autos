package com.ufinet.autos.domain.service;

import com.ufinet.autos.domain.model.Car;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.regex.Pattern;

@Component
public class CarValidationService {

    // Expresión regular para un formato de matrícula común (ej. ABC-123 o ABC-1234)
    // Ajústala según el formato de tu país.
    private static final Pattern PLATE_PATTERN = Pattern.compile("^[A-Z]{3}\\d{3,4}$");

    public void validate(Car car) {
        if (car.getBrand() == null || car.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca del coche no puede estar vacía.");
        }
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo del coche no puede estar vacío.");
        }
        if (car.getPlate() == null || car.getPlate().trim().isEmpty()) {
            throw new IllegalArgumentException("La matrícula del coche no puede estar vacía.");
        }
        if (car.getYear() == null) {
            throw new IllegalArgumentException("El año del coche no puede estar vacío.");
        }

        // Regla de negocio: La matrícula debe tener un formato válido
        if (!PLATE_PATTERN.matcher(car.getPlate().toUpperCase()).matches()) {
            throw new IllegalArgumentException("El formato de la matrícula no es válido. Debe ser como 'ABC123'.");
        }

        // Regla de negocio: El año del coche no puede ser en el futuro
        if (car.getYear() > Year.now().getValue()) {
            throw new IllegalArgumentException("El año del coche no puede ser superior al año actual.");
        }
    }
}