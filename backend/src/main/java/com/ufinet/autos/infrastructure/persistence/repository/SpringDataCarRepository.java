package com.ufinet.autos.infrastructure.persistence.repository;

import com.ufinet.autos.infrastructure.persistence.entity.CarJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

//JpaSpecificationExecutor consultas dinamicas
public interface SpringDataCarRepository extends JpaRepository<CarJpaEntity, Long>, JpaSpecificationExecutor<CarJpaEntity> {
    List<CarJpaEntity> findByUserId(Long userId);
    Optional<CarJpaEntity> findByPlate(String plate);
}
