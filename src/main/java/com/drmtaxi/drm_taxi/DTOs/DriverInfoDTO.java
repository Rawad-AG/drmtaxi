package com.drmtaxi.drm_taxi.DTOs;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import com.drmtaxi.drm_taxi.Utils.Car;
import com.drmtaxi.drm_taxi.Utils.Gender;
import com.drmtaxi.drm_taxi.Utils.Location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record DriverInfoDTO(
        @NotNull Long id,
        @NotBlank String firstName,
        @NotBlank String fatherName,
        @NotBlank String lastName,
        @NotBlank String username,
        boolean isDriver,
        @NotNull @Past Instant createdAt,
        @NotNull Car car,
        @NotNull Location livingLocation,
        @NotNull LocalTime startWorkHour,
        @NotNull LocalTime endWorkHour,
        boolean available,
        double amountOwed,
        LocalDate paymentDeadline,
        boolean inWork,
        @NotNull Gender gender) {

}
