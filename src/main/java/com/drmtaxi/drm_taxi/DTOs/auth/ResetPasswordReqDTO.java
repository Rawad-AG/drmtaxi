package com.drmtaxi.drm_taxi.DTOs.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordReqDTO(
        @NotBlank(message = "token is required") String token,
        @NotBlank(message = "password is required") @Size(min = 8, message = "password must be at least 8 characters long") String newPassword) {

}
