package com.ufinet.autos.application.port.in;

import com.ufinet.autos.domain.model.Car;
import java.util.List;

public interface CarUseCase {
    List<Car> listByUser(Long userId);
    Car getById(Long id);
    Car create(Car car);
    Car update(Long id, Car car);
    void delete(Long id);
}
