package com.drmtaxi.drm_taxi.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsernamePasswordDTO(
    @NotBlank(message = "username is required")
    String username,
    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 characters long")
    String password
) {
    
}
