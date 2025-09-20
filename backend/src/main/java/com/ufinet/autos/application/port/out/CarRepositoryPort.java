package com.ufinet.autos.application.port.out;

import com.ufinet.autos.domain.model.Car;
import java.util.List;
import java.util.Optional;

public interface CarRepositoryPort {
    List<Car> findByCriteria(Long userId, String search, String brand, Integer year);
    List<Car> findByUserId(Long userId);
    Optional<Car> findById(Long id);
    Car save(Car car);
    void deleteById(Long id);
    Optional<Car> findByPlate(String plate);
}
