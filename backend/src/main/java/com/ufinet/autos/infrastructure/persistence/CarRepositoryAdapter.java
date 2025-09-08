package com.ufinet.autos.infrastructure.persistence;

import com.ufinet.autos.application.port.out.CarRepositoryPort;
import com.ufinet.autos.domain.model.Car;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CarRepositoryAdapter implements CarRepositoryPort {

    private final SpringDataCarRepository repo;

    public CarRepositoryAdapter(SpringDataCarRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Car> findByUserId(Long userId) {
        return repo.findByUserId(userId).stream().map(CarJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Car> findById(Long id) {
        return repo.findById(id).map(CarJpaEntity::toDomain);
    }

    @Override
    public Car save(Car car) {
        CarJpaEntity entity = CarJpaEntity.fromDomain(car);
        CarJpaEntity saved = repo.save(entity);
        return saved.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // AHORA (Correcto)
    @Override
    public Optional<Car> findByPlate(String plate) {
        return repo.findByPlate(plate)
                .map(CarJpaEntity::toDomain);
    }
}
