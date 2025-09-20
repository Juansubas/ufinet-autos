package com.ufinet.autos.infrastructure.persistence.entity;

import com.ufinet.autos.domain.model.Car;
import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class CarJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private Integer year;

    @Column(unique = true)
    private String plate;

    private String color;

    @Column(name = "user_id")
    private Long userId;

    public CarJpaEntity() {
    }

    public CarJpaEntity(Long id, String brand, String model, Integer year, String plate, String color, Long userId) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.plate = plate;
        this.color = color;
        this.userId = userId;
    }

    public static CarJpaEntity fromDomain(Car c) {
        return new CarJpaEntity(
                c.getId(),
                c.getBrand(),
                c.getModel(),
                c.getYear(),
                c.getPlate(),
                c.getColor(),
                c.getUserId()
        );
    }

    public Car toDomain() {
        return new Car(this.id, this.brand, this.model, this.year, this.plate, this.color, this.userId);
    }

    // Getters y setters manuales
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
