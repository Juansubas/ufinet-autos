package com.ufinet.autos.infrastructure.persistence.specification;

import com.ufinet.autos.infrastructure.persistence.entity.CarJpaEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarSpecification {

    public Specification<CarJpaEntity> findByCriteria(Long userId, String search, String brand, Integer year) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Condición obligatoria: siempre filtra por el ID del usuario
            predicates.add(criteriaBuilder.equal(root.get("userId"), userId));

            // Condiciones opcionales: se añaden solo si no son nulas/vacías
            if (brand != null && !brand.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
            }

            if (year != null) {
                predicates.add(criteriaBuilder.equal(root.get("year"), year));
            }

            if (search != null && !search.isBlank()) {
                Predicate plateMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("plate")), "%" + search.toLowerCase() + "%");
                Predicate modelMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), "%" + search.toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(plateMatch, modelMatch));
            }

            // Combina todas las condiciones en una sola consulta
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}