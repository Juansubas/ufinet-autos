package com.ufinet.autos.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataCarRepository extends JpaRepository<CarJpaEntity, Long> {
    List<CarJpaEntity> findByUserId(Long userId);
    Optional<CarJpaEntity> findByPlate(String plate);
}
