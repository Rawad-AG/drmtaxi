package com.drmtaxi.drm_taxi.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ForgetPasswordDTO(
    @NotBlank(message = "token is required")
    String token,
    @NotNull(message = "id is required")
    Long id,
    @NotBlank
    @Size(min = 8, message = "Password must have at least 8 characters")
    String password
) {
    
}
