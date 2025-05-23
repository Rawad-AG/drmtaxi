package com.drmtaxi.drm_taxi.Entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "DriverPayments")
@Table(name = "drivers_payments")
public class DriverPaymentsEntity {

    @Id
    @SequenceGenerator(name = "driver_payments_id_sequence", sequenceName = "driver_payments_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_payments_id_sequence")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "driver_id")
    private DriverInfoEntity driver;

    @CreationTimestamp
    private Instant createdAt;

    private double amount;

}
