package com.ufinet.autos.infrastructure.persistence.adapter;

import com.ufinet.autos.application.port.out.CarRepositoryPort;
import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.infrastructure.persistence.entity.CarJpaEntity;
import com.ufinet.autos.infrastructure.persistence.specification.CarSpecification;
import com.ufinet.autos.infrastructure.persistence.repository.SpringDataCarRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CarRepositoryAdapter implements CarRepositoryPort {

    private final SpringDataCarRepository repo;
    private final CarSpecification carSpecification;

    public CarRepositoryAdapter(SpringDataCarRepository repo, CarSpecification carSpecification) {
        this.repo = repo;
        this.carSpecification = carSpecification;
    }

    @Override
    public List<Car> findByCriteria(Long userId, String search, String brand, Integer year) {
        // 1. Construye la especificación con los filtros recibidos
        Specification<CarJpaEntity> spec = carSpecification.findByCriteria(userId, search, brand, year);

        // 2. Ejecuta la búsqueda y mapea el resultado al modelo de dominio
        return repo.findAll(spec).stream()
                .map(CarJpaEntity::toDomain)
                .collect(Collectors.toList());
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

    @Override
    public Optional<Car> findByPlate(String plate) {
        return repo.findByPlate(plate)
                .map(CarJpaEntity::toDomain);
    }
}
