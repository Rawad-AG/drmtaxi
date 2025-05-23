package com.drmtaxi.drm_taxi.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotBlank String username,
        @NotBlank @Size(min = 8) String password) {
}
