package com.drmtaxi.drm_taxi.DTOs;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.drmtaxi.drm_taxi.Utils.Gender;
import com.drmtaxi.drm_taxi.Utils.Location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SignupDriverDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String fatherName,
        @NotNull Gender gender,
        @NotBlank String phoneNumber,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String licenseNumber,
        @NotBlank String carCompany,
        @NotBlank String carModel,
        @NotBlank String carNumber,
        @NotBlank String carColor,
        @Positive long idNumber,
        @NotNull Location livingLocation,
        @NotNull @Past LocalDateTime dateOfBirth,
        @NotNull Location locationOfBirth,
        @NotNull LocalTime startWorkHour,
        @NotNull LocalTime endWorkHour) {

}
