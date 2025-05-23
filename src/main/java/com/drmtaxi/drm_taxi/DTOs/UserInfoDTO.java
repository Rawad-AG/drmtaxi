package com.drmtaxi.drm_taxi.DTOs;

import java.time.Instant;

import com.drmtaxi.drm_taxi.Utils.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record UserInfoDTO(
                @NotNull Long id,
                @NotBlank String firstName,
                String lastName,
                @NotBlank String username,
                boolean isDriver,
                @NotNull @Past Instant createdAt,
                @NotNull Gender gender) {
}
