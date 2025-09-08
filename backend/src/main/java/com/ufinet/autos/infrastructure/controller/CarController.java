package com.ufinet.autos.infrastructure.controller;

import com.ufinet.autos.application.port.in.CarUseCase;
import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.infrastructure.controller.dto.CarRequest;
import com.ufinet.autos.infrastructure.security.JwtUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarUseCase carUseCase;

    public CarController(CarUseCase carUseCase) {
        this.carUseCase = carUseCase;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof JwtUserDetails) {
            return ((JwtUserDetails) auth.getPrincipal()).getUserId();
        }
        throw new RuntimeException("User not authenticated");
    }

    @GetMapping
    public ResponseEntity<?> list() {
        Long userId = getCurrentUserId();
        List<Car> cars = carUseCase.listByUser(userId);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Car car = carUseCase.getById(id);
        if (car == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Car not found"));
        }

        Long userId = getCurrentUserId();
        if (!car.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("message", "Forbidden: Car does not belong to user"));
        }

        return ResponseEntity.ok(car);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CarRequest request) {
        Long userId = getCurrentUserId();
        Car domain = request.toDomain();
        domain.setUserId(userId);
        Car saved = carUseCase.create(domain);

        return ResponseEntity.status(201).body(Map.of(
                "message", "Car created successfully",
                "car", saved
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CarRequest request) {
        Car existing = carUseCase.getById(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Car not found"));
        }

        Car domain = request.toDomain();
        Car updated = carUseCase.update(id, domain);

        return ResponseEntity.ok(Map.of(
                "message", "Car updated successfully",
                "car", updated
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Car existing = carUseCase.getById(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Car not found"));
        }

        carUseCase.delete(id);
        return ResponseEntity.ok(Map.of("message", "Car deleted successfully"));
    }
}
