package com.drmtaxi.drm_taxi.DTOs;

import org.hibernate.annotations.NotFound;

import com.drmtaxi.drm_taxi.Utils.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupUserDTO(
        @NotBlank String firstName,
        String lastName,
        @NotFound Gender gender,
        @NotBlank String username,
        @NotBlank @Size(min = 8) String password) {
}
