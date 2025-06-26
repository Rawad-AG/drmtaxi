package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "Car")
@Table(name = "cars")
public class CarEntity {
    @Id
    @SequenceGenerator(name = "cars_id_sequence", sequenceName = "cars_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cars_id_sequence")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private DriverEntity driver;

    private String company;
    private String model;
    private LocalDate year;
    private String licenseNumber;
    private String color;
    private int gasConsumption;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public CarEntity(DriverEntity driver, String company, String model, LocalDate year, String licenseNumber,
            String color) {
        this.driver = driver;
        this.company = company;
        this.model = model;
        this.year = year;
        this.licenseNumber = licenseNumber;
        this.color = color;
        this.gasConsumption = 10;
    }

}
