package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "DriverFinancial")
@Table(name = "drivers_financials")
public class DriverFinancialEntity {
    @Id
    @SequenceGenerator(name = "drivers_financials_id_sequence", sequenceName = "drivers_financials_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drivers_financials_id_sequence")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id")
    private DriverEntity driver;

    private int workingHours;
    private int expectedHours;

    private int ordersCount;

    private int totalDistance;

    private int approximateGasConsumption;

    private double totalMoney;

    private double driverShare;

    private double companyShare;

    private boolean paid;

    private Instant paidAt;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}
