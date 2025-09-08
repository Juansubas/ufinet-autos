package com.ufinet.autos.infrastructure.controller.dto;

import com.ufinet.autos.domain.model.Car;

public class CarRequest {

    private String brand;
    private String model;
    private Integer year;
    private String plate;
    private String color;

    // Constructor vacío (necesario para Spring)
    public CarRequest() {
    }

    // Getters y Setters
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

    // Método para convertir a dominio
    public Car toDomain() {

        return new Car(null, brand, model, year, plate, color, null);
    }
}
